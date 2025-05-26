package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlightController {

    private final FlightStorage storage;
    private static final Pattern FLIGHT_ID_PATTERN = Pattern.compile("^[A-Z]{3}\\d{3}$");

    public FlightController() {
        this.storage = new FlightStorage();
    }

    public Response createFlight(String id,
            Plane plane,
            Location departure,
            Location arrival,
            Location scale,
            LocalDateTime departureDate,
            int arrivalHours,
            int arrivalMinutes,
            int scaleHours,
            int scaleMinutes) {

      
        if (id == null || !FLIGHT_ID_PATTERN.matcher(id).matches()) {
            return new Response(
                    "El ID debe tener formato XXXYYY (3 letras mayúsculas + 3 dígitos).",
                    Status.BAD_REQUEST
            );
        }
        if (storage.existsById(id)) {
            return new Response("Ya existe un vuelo con este ID.", Status.BAD_REQUEST);
        }

       
        if (plane == null) {
            return new Response("El avión es requerido.", Status.BAD_REQUEST);
        }
        if (departure == null || arrival == null) {
            return new Response(
                    "Las localizaciones de salida y llegada son requeridas.",
                    Status.BAD_REQUEST
            );
        }

       
        if (scale == null && (scaleHours != 0 || scaleMinutes != 0)) {
            return new Response(
                    "Si no hay escala, la duración de escala debe ser 00:00.",
                    Status.BAD_REQUEST
            );
        }

       
        if (departureDate == null) {
            return new Response("La fecha de salida es requerida.", Status.BAD_REQUEST);
        }

    
        int totalMinutes
                = arrivalHours * 60 + arrivalMinutes
                + scaleHours * 60 + scaleMinutes;
        if (totalMinutes <= 0) {
            return new Response(
                    "La duración total del vuelo debe ser mayor que 00:00.",
                    Status.BAD_REQUEST
            );
        }

        try {
       
            Flight flight = new Flight(
                    id,
                    plane,
                    departure,
                    scale,
                    arrival,
                    departureDate,
                    arrivalHours,
                    arrivalMinutes,
                    scaleHours,
                    scaleMinutes
            );
            storage.saveFlight(flight);

          
            Flight copy = new Flight(
                    flight.getId(),
                    flight.getPlane(),
                    flight.getDepartureLocation(),
                    flight.getScaleLocation(),
                    flight.getArrivalLocation(),
                    flight.getDepartureDate(),
                    flight.getHoursDurationArrival(),
                    flight.getMinutesDurationArrival(),
                    flight.getHoursDurationScale(),
                    flight.getMinutesDurationScale()
            );
            return new Response("Vuelo creado exitosamente.", Status.CREATED, copy);

        } catch (IllegalArgumentException iae) {
            return new Response(
                    "Error en datos del vuelo: " + iae.getMessage(),
                    Status.BAD_REQUEST
            );
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new Response(
                    "Error al guardar el vuelo.",
                    Status.INTERNAL_SERVER_ERROR
            );
        }
    }

    public List<Flight> getFlights() {
        List<Flight> all = storage.getAllFlights();
        all.sort(Comparator.comparing(Flight::getDepartureDate));
        return all.stream()
                .map(f -> new Flight(
                f.getId(),
                f.getPlane(),
                f.getDepartureLocation(),
                f.getScaleLocation(),
                f.getArrivalLocation(),
                f.getDepartureDate(),
                f.getHoursDurationArrival(),
                f.getMinutesDurationArrival(),
                f.getHoursDurationScale(),
                f.getMinutesDurationScale()
        ))
                .collect(Collectors.toList());
    }

    public Response addPassengerToFlight(String flightId, long passengerId) {
        if (!storage.existsById(flightId)) {
            return new Response("Vuelo no encontrado.", Status.BAD_REQUEST);
        }
        if (passengerId < 0 || String.valueOf(passengerId).length() > 15) {
            return new Response(
                    "El ID de pasajero debe ser ≥ 0 y hasta 15 dígitos.",
                    Status.BAD_REQUEST
            );
        }
        boolean ok = storage.addPassengerToFlight(flightId, passengerId);
        if (!ok) {
            return new Response(
                    "No se pudo añadir el pasajero al vuelo.",
                    Status.INTERNAL_SERVER_ERROR
            );
        }
        return new Response("Pasajero añadido al vuelo.", Status.OK);
    }

    public Response delayFlight(String flightId, int hours, int minutes) {
        if (!storage.existsById(flightId)) {
            return new Response("Vuelo no encontrado.", Status.BAD_REQUEST);
        }
        if (hours < 0 || minutes < 0 || (hours == 0 && minutes == 0)) {
            return new Response(
                    "El tiempo de retraso debe ser mayor que 00:00.",
                    Status.BAD_REQUEST
            );
        }
        boolean ok = storage.delayFlight(flightId, hours, minutes);
        if (!ok) {
            return new Response(
                    "No se pudo aplicar el retraso.",
                    Status.INTERNAL_SERVER_ERROR
            );
        }
        return new Response("Retraso aplicado correctamente.", Status.OK);
    }
}
