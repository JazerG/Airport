package core.models.storage;

import core.models.Plane;
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

public class PlaneStorage {

    private static final String FILE_PATH = "json/planes.json";

    private void ensureFileExists() throws IOException {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            Files.write(Paths.get(FILE_PATH), "[]".getBytes(), StandardOpenOption.CREATE);
        }
    }

    public boolean addPlane(Plane plane) {
        try {
            ensureFileExists();

            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);

            // 1) Verificar duplicados
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("id").equals(plane.getId())) {
                    return false; // ya existe
                }
            }

            // 2) Construir objeto JSON
            JSONObject obj = new JSONObject();
            obj.put("id", plane.getId());
            obj.put("brand", plane.getBrand());
            obj.put("model", plane.getModel());
            obj.put("maxCapacity", plane.getMaxCapacity());
            obj.put("airline", plane.getAirline());

            // 3) Añadir al array y grabar
            jsonArray.put(obj);
            try ( FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(jsonArray.toString(4));
            }

            return true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Plane> getAllPlanes() {
        List<Plane> planes = new ArrayList<>();
        try {
            ensureFileExists();

            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                planes.add(new Plane(
                        obj.getString("id"),
                        obj.getString("brand"),
                        obj.getString("model"),
                        obj.getInt("maxCapacity"),
                        obj.getString("airline")
                ));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return planes;
    }

    public static boolean existsById(String id) {
        return new PlaneStorage()
                .getAllPlanes()
                .stream()
                .anyMatch(p -> p.getId().equals(id));
    }

    public Plane getById(String id) {
        for (Plane p : getAllPlanes()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No se encontró un avión con ID: " + id);
    }
}
