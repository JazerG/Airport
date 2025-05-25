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
        if (id < 0) {
            return new Response("Id must be positive", Status.BAD_REQUEST);
        }

        if (firstname == null || firstname.isEmpty()) {
            return new Response("Firstname cannot be empty", Status.BAD_REQUEST);
        }

        if (lastname == null || lastname.isEmpty()) {
            return new Response("Lastname cannot be empty", Status.BAD_REQUEST);
        }

        if (birthDate == null) {
            return new Response("Birth date is required", Status.BAD_REQUEST);
        }

        if (country == null || country.isEmpty()) {
            return new Response("Country cannot be empty", Status.BAD_REQUEST);
        }

        Passenger newPassenger = new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country);

        PassengerStorage storage = new PassengerStorage();
        boolean saved = storage.addPassenger(newPassenger);

        if (!saved) {
            return new Response("Passenger with this ID already exists.", Status.BAD_REQUEST);
        }

        return new Response("Passenger successfully added", Status.OK, newPassenger);
    }
   public static List<Passenger> getPassengers() {
        return PassengerStorage.getAllPassengers();
    }
}
    

