// core/controllers/LocationController.java
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Location;
import core.models.storage.LocationStorage;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocationController {

    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z]{3}$");
    private final LocationStorage storage = new LocationStorage();

    public Response addLocation(String id,
            String name,
            String city,
            String country,
            Double latitude,
            Double longitude) {
        // ID
        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            return new Response("El ID debe ser 3 letras mayúsculas.", Status.BAD_REQUEST);
        }
        if (storage.existsById(id)) {
            return new Response("Ya existe una localización con este ID.", Status.BAD_REQUEST);
        }

        // Campos obligatorios
        if (name == null || name.trim().isEmpty()) {
            return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (city == null || city.trim().isEmpty()) {
            return new Response("La ciudad no puede estar vacía.", Status.BAD_REQUEST);
        }
        if (country == null || country.trim().isEmpty()) {
            return new Response("El país no puede estar vacío.", Status.BAD_REQUEST);
        }

        // Rango de coordenadas
        if (latitude == null || latitude < -90 || latitude > 90) {
            return new Response("La latitud debe estar entre −90 y 90.", Status.BAD_REQUEST);
        }
        if (longitude == null || longitude < -180 || longitude > 180) {
            return new Response("La longitud debe estar entre −180 y 180.", Status.BAD_REQUEST);
        }

        // Hasta 4 decimales
        try {
            if (new BigDecimal(latitude.toString()).scale() > 4) {
                return new Response("La latitud admite hasta 4 decimales.", Status.BAD_REQUEST);
            }
            if (new BigDecimal(longitude.toString()).scale() > 4) {
                return new Response("La longitud admite hasta 4 decimales.", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Error al procesar las coordenadas.", Status.BAD_REQUEST);
        }

        // Construir y persistir
        Location loc = new Location(id, name, city, country, latitude, longitude);
        boolean saved = storage.addLocation(loc);
        if (!saved) {
            return new Response("No se pudo guardar la localización.", Status.INTERNAL_SERVER_ERROR);
        }

        // Devolver copia (Prototype)
        Location copy = new Location(
                loc.getAirportId(),
                loc.getAirportName(),
                loc.getAirportCity(),
                loc.getAirportCountry(),
                loc.getAirportLatitude(),
                loc.getAirportLongitude()
        );
        return new Response("Localización creada exitosamente.", Status.CREATED, copy);
    }

    public List<Location> getLocations() {
        return storage.getAllLocations().stream()
                .sorted(Comparator.comparing(Location::getAirportId))
                .map(l -> new Location(
                l.getAirportId(),
                l.getAirportName(),
                l.getAirportCity(),
                l.getAirportCountry(),
                l.getAirportLatitude(),
                l.getAirportLongitude()
        ))
                .collect(Collectors.toList());
    }
}
