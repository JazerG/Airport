/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Plane;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jazer
 */
public class PlaneStorage {

    private final String filePath = "json/planes.json";

    public boolean addPlane(Plane plane) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            // Validar si el ID ya existe
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("id").equals(plane.getId())) {
                    return false; // ID duplicado
                }
            }

            JSONObject newPlane = new JSONObject();
            newPlane.put("maxCapacity", plane.getMaxCapacity());
            newPlane.put("model", plane.getModel());
            newPlane.put("id", plane.getId());
            newPlane.put("airline", plane.getAirline());
            newPlane.put("brand", plane.getBrand());

            // Agregar al array y guardar archivo
            jsonArray.put(newPlane);
            Files.write(Paths.get(filePath), jsonArray.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Plane> getAllPlanes() {
        List<Plane> planes = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/planes.json")));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Plane p = new Plane(
                        obj.getString("id"),
                        obj.getString("brand"),
                        obj.getString("model"),
                        obj.getInt("maxCapacity"),
                        obj.getString("airline")               
                );
                planes.add(p);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return planes;
    }
}
