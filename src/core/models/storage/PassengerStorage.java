/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Passenger;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PassengerStorage {

    private final String filePath = "json/passengers.json";

    public boolean addPassenger(Passenger passenger) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            // Validar si el ID ya existe
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getLong("id") == passenger.getId()) {
                    return false; // ID duplicado
                }
            }

            JSONObject newPassenger = new JSONObject();
            newPassenger.put("id", passenger.getId());
            newPassenger.put("firstname", passenger.getFirstname());
            newPassenger.put("lastname", passenger.getLastname());
            newPassenger.put("birthDate", passenger.getBirthDate());
            newPassenger.put("countryPhoneCode", passenger.getCountryPhoneCode());
            newPassenger.put("phone", String.valueOf(passenger.getPhone()));
            newPassenger.put("country", passenger.getCountry());

            // Agregar al array y guardar archivo
            jsonArray.put(newPassenger);
            Files.write(Paths.get(filePath), jsonArray.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassenger(Passenger passenger) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            boolean found = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getLong("id") == passenger.getId()) {
                    obj.put("firstname", passenger.getFirstname());
                    obj.put("lastname", passenger.getLastname());
                    obj.put("birthDate", passenger.getBirthDate().toString());
                    obj.put("countryPhoneCode", passenger.getCountryPhoneCode());
                    obj.put("phone", String.valueOf(passenger.getPhone()));
                    obj.put("country", passenger.getCountry());
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }

            Files.write(
                    Paths.get(filePath),
                    jsonArray.toString(4).getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/passengers.json")));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Passenger p = new Passenger(
                        obj.getLong("id"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        LocalDate.parse(obj.getString("birthDate")),
                        obj.getInt("countryPhoneCode"),
                        obj.getLong("phone"),
                        obj.getString("country")
                );
                passengers.add(p);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return passengers;
    }

    public static boolean existsById(long id) {
        List<Passenger> passengers = getAllPassengers();
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public Passenger getById(long id) {
        List<Passenger> passengers = getAllPassengers();
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

}
