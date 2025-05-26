package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Passenger;
import core.models.storage.PassengerStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PassengerController {

    public static Response addPassenger(long id,
            String firstname,
            String lastname,
            LocalDate birthDate,
            int countryPhoneCode,
            long phone,
            String country) {
        // Validaciones de ID
        if (id < 0 || String.valueOf(id).length() > 15) {
            return new Response(
                    "El ID debe ser mayor o igual a 0 y tener máximo 15 dígitos.",
                    Status.BAD_REQUEST
            );
        }
        if (PassengerStorage.existsById(id)) {
            return new Response("Ya existe un pasajero con este ID.", Status.BAD_REQUEST);
        }

        // Validación de nombre y apellido
        if (firstname == null || firstname.trim().isEmpty()) {
            return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (lastname == null || lastname.trim().isEmpty()) {
            return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
        }

        // Fecha de nacimiento válida y no futura
        if (birthDate == null) {
            return new Response("La fecha de nacimiento es requerida.", Status.BAD_REQUEST);
        }
        if (birthDate.isAfter(LocalDate.now())) {
            return new Response("La fecha de nacimiento no puede ser futura.", Status.BAD_REQUEST);
        }

        // Validaciones de teléfono
        if (countryPhoneCode < 0 || String.valueOf(countryPhoneCode).length() > 3) {
            return new Response(
                    "El código telefónico debe ser mayor o igual a 0 y tener máximo 3 dígitos.",
                    Status.BAD_REQUEST
            );
        }
        if (phone < 0 || String.valueOf(phone).length() > 11) {
            return new Response(
                    "El número de teléfono debe ser mayor o igual a 0 y tener máximo 11 dígitos.",
                    Status.BAD_REQUEST
            );
        }

        // Validación de país
        if (country == null || country.trim().isEmpty()) {
            return new Response("El país no puede estar vacío.", Status.BAD_REQUEST);
        }

        // Creación y almacenamiento
        Passenger model = new Passenger(id, firstname, lastname, birthDate,
                countryPhoneCode, phone, country);
        PassengerStorage storage = new PassengerStorage();
        boolean saved = storage.addPassenger(model);
        if (!saved) {
            return new Response("No se pudo guardar el pasajero.", Status.INTERNAL_SERVER_ERROR);
        }

        // Devolver copia (Prototype)
        Passenger copy = new Passenger(
                model.getId(),
                model.getFirstname(),
                model.getLastname(),
                model.getBirthDate(),
                model.getCountryPhoneCode(),
                model.getPhone(),
                model.getCountry()
        );
        return new Response("Pasajero creado exitosamente.", Status.CREATED, copy);
    }

    public static Response updatePassenger(long id,
            String firstname,
            String lastname,
            LocalDate birthDate,
            int countryPhoneCode,
            long phone,
            String country) {
        // 1) Verificar existencia
        if (!PassengerStorage.existsById(id)) {
            return new Response("No existe un pasajero con este ID.", Status.BAD_REQUEST);
        }

        // 2) Validación de campos (igual que en add, excepto ID/uniquidad)
        if (firstname == null || firstname.trim().isEmpty()) {
            return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (lastname == null || lastname.trim().isEmpty()) {
            return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (birthDate == null) {
            return new Response("La fecha de nacimiento es requerida.", Status.BAD_REQUEST);
        }
        if (birthDate.isAfter(LocalDate.now())) {
            return new Response("La fecha de nacimiento no puede ser futura.", Status.BAD_REQUEST);
        }
        if (countryPhoneCode < 0 || String.valueOf(countryPhoneCode).length() > 3) {
            return new Response(
                    "El código telefónico debe ser mayor o igual a 0 y tener máximo 3 dígitos.",
                    Status.BAD_REQUEST
            );
        }
        if (phone < 0 || String.valueOf(phone).length() > 11) {
            return new Response(
                    "El número de teléfono debe ser mayor o igual a 0 y tener máximo 11 dígitos.",
                    Status.BAD_REQUEST
            );
        }
        if (country == null || country.trim().isEmpty()) {
            return new Response("El país no puede estar vacío.", Status.BAD_REQUEST);
        }

        // 3) Actualizar en storage
        Passenger updated = new Passenger(id, firstname, lastname, birthDate,
                countryPhoneCode, phone, country);
        PassengerStorage storage = new PassengerStorage();
        boolean ok = storage.updatePassenger(updated);
        if (!ok) {
            return new Response("No se pudo actualizar el pasajero.", Status.INTERNAL_SERVER_ERROR);
        }

        // 4) Devolver copia (Prototype)
        Passenger copy = new Passenger(
                updated.getId(),
                updated.getFirstname(),
                updated.getLastname(),
                updated.getBirthDate(),
                updated.getCountryPhoneCode(),
                updated.getPhone(),
                updated.getCountry()
        );
        return new Response("Pasajero actualizado exitosamente.", Status.OK, copy);
    }

    public static List<Passenger> getPassengers() {
        List<Passenger> list = new ArrayList<>(PassengerStorage.getAllPassengers());
        list.sort(Comparator.comparingLong(Passenger::getId));
        return list;
    }

    public static List<String> getAllPassengerIds() {
        List<String> ids = new ArrayList<>();
        for (Passenger p : getPassengers()) {
            ids.add(String.valueOf(p.getId()));
        }
        return ids;
    }
}
