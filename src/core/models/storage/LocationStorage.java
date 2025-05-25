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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
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
            newLocation.put("airportId", location.getAirportId());
            newLocation.put("airportName", location.getAirportName());
            newLocation.put("airportCity", location.getAirportCity());
            newLocation.put("airportCountry", location.getAirportCountry());
            newLocation.put("airportLatitude", location.getAirportLatitude());
            newLocation.put("airportLongitude", location.getAirportLongitude());
            
            

            // Agregar al array y guardar archivo
            jsonArray.put(newLocation);
            Files.write(Paths.get(filePath), jsonArray.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/locations.json")));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Location p = new Location(
                        obj.getString("airportId"),
                        obj.getString("airportName"),
                        obj.getString("airportCity"),
                        obj.getString("airportCountry"),
                        obj.getDouble("airportLatitude"),
                        obj.getDouble("airportLongitude")
                        
                );
                locations.add(p);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return locations;
    }
    public Location getById(String id) {
    for (Location l : getAllLocations()) {
        if (l.getAirportId().equals(id)) {
            return l;
        }
    }
    throw new IllegalArgumentException("No se encontró una ubicación con ID: " + id);
}
}
