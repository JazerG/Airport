/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Passenger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.json.JSONArray;
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
}
