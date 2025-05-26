// core/models/storage/LocationStorage.java
package core.models.storage;

import core.models.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LocationStorage {

    private static final String FILE_PATH = "json/locations.json";

    public boolean addLocation(Location location) {
        try {
            // Aseguramos que el fichero existe
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                Files.write(Paths.get(FILE_PATH), "[]".getBytes(), StandardOpenOption.CREATE);
            }

            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);

            // ID duplicado?
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("airportId").equals(location.getAirportId())) {
                    return false;
                }
            }

            // Construimos el objeto JSON
            JSONObject newLocation = new JSONObject();
            newLocation.put("airportId", location.getAirportId());
            newLocation.put("airportName", location.getAirportName());
            newLocation.put("airportCity", location.getAirportCity());
            newLocation.put("airportCountry", location.getAirportCountry());
            newLocation.put("airportLatitude", location.getAirportLatitude());
            newLocation.put("airportLongitude", location.getAirportLongitude());

            // Lo agregamos y guardamos
            jsonArray.put(newLocation);
            try ( FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(jsonArray.toString(4));
            }
            return true;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                locations.add(new Location(
                        obj.getString("airportId"),
                        obj.getString("airportName"),
                        obj.getString("airportCity"),
                        obj.getString("airportCountry"),
                        obj.getDouble("airportLatitude"),
                        obj.getDouble("airportLongitude")
                ));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public boolean existsById(String id) {
        for (Location l : getAllLocations()) {
            if (l.getAirportId().equals(id)) {
                return true;
            }
        }
        return false;
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
