/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Passenger;
import core.models.storage.PassengerStorage;
import java.util.List;
import java.time.LocalDate;

public class PassengerController {

    public static Response addPassenger(long id, String firstname, String lastname, LocalDate birthDate, int countryPhoneCode, long phone, String country) {
        if (String.valueOf(id).isBlank()) {
        return new Response("El ID no puede estar vacío.", Status.BAD_REQUEST);
    }

    if (String.valueOf(countryPhoneCode).isBlank()) {
        return new Response("El código telefónico no puede estar vacío.", Status.BAD_REQUEST);
    }

    if (String.valueOf(phone).isBlank()) {
        return new Response("El número de teléfono no puede estar vacío.", Status.BAD_REQUEST);
    }

    // Validar ID
    if (id < 0 || String.valueOf(id).length() > 15) {
        return new Response("El ID debe ser mayor o igual a 0 y tener máximo 15 dígitos.", Status.BAD_REQUEST);
    }

    if (PassengerStorage.existsById(id)) {
        return new Response("Ya existe un pasajero con este ID.", Status.BAD_REQUEST);
    }

    // Validar campos de texto vacíos
    if (firstname == null || firstname.trim().isEmpty()) {
        return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
    }

    if (lastname == null || lastname.trim().isEmpty()) {
        return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
    }

    if (country == null || country.trim().isEmpty()) {
        return new Response("El país no puede estar vacío.", Status.BAD_REQUEST);
    }

    // Validar fecha de nacimiento
    if (birthDate == null) {
        return new Response("La fecha de nacimiento es requerida.", Status.BAD_REQUEST);
    }

    // Validar código de país
    if (countryPhoneCode < 0 || String.valueOf(countryPhoneCode).length() > 3) {
        return new Response("El código telefónico debe ser mayor o igual a 0 y tener máximo 3 dígitos.", Status.BAD_REQUEST);
    }

    // Validar teléfono
    if (phone < 0 || String.valueOf(phone).length() > 11) {
        return new Response("El número de teléfono debe ser mayor o igual a 0 y tener máximo 11 dígitos.", Status.BAD_REQUEST);
    }
        Passenger newPassenger = new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country);

        PassengerStorage storage = new PassengerStorage();
        boolean saved = storage.addPassenger(newPassenger);

        if (!saved) {
            return new Response("No se pudo guardar el pasajero.", Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Pasajero creado exitosamente.", Status.CREATED, newPassenger);
    }

    public static List<Passenger> getPassengers() {
        return PassengerStorage.getAllPassengers();
    }
}
