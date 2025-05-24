/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

/**
 *
 * @author jazer
 */
import core.models.Location;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationStorage {

    private final String filePath = "json/locations.json";

    public boolean addLocation(Location location) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            // Validar si el ID ya existe
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("airportId").equals(location.getAirportId())) {
                    return false; // ID duplicado
                }
            }          
            
            
            JSONObject newLocation = new JSONObject();
            newLocation.put("AirportId", location.getAirportId());
            newLocation.put("AirportName", location.getAirportName());
            newLocation.put("AirportCity", location.getAirportCity());
            newLocation.put("AirportCountry", location.getAirportCountry());
            newLocation.put("AirportLatitude", location.getAirportLatitude());
            newLocation.put("AirportLongitude", location.getAirportLongitude());
            
            

            // Agregar al array y guardar archivo
            jsonArray.put(newLocation);
            Files.write(Paths.get(filePath), jsonArray.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
