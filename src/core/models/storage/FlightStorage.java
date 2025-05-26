package core.models.storage;

import core.models.Flight;
import core.models.Passenger;
import core.models.storage.PassengerStorage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FlightStorage {

    private static final String FILE_PATH = "json/flights.json";

    private void ensureFileExists() throws IOException {
        File file = new File(FILE_PATH);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists()) {
            Files.write(
                    Paths.get(FILE_PATH),
                    "[]".getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("[FlightStorage] Se creó flights.json vacío en: "
                    + file.getAbsolutePath());
        }
    }

    public void saveFlight(Flight flight) throws IOException {
        System.out.println("[FlightStorage] Guardando vuelo: " + flight.getId());
        ensureFileExists();

        List<Flight> flights = loadFlights();
        for (Flight f : flights) {
            if (f.getId().equals(flight.getId())) {
                throw new IllegalArgumentException("Ya existe un vuelo con ese ID.");
            }
        }
        flights.add(flight);
        saveAll(flights);
    }

    private List<Flight> loadFlights() throws IOException {
        ensureFileExists();
        String content = Files.readString(Paths.get(FILE_PATH));
        System.out.println("[FlightStorage] Leyendo JSON de vuelos: " + content);

        List<Flight> flights = new ArrayList<>();
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            flights.add(Flight.fromJson(array.getJSONObject(i)));
        }
        return flights;
    }

    private void saveAll(List<Flight> flights) throws IOException {
        JSONArray array = new JSONArray();
        for (Flight f : flights) {
            array.put(f.toJson());
        }
        String serialized = array.toString(4);
        try ( FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(serialized);
            writer.flush();
        }
        System.out.println("[FlightStorage] Guardado JSON de vuelos: " + serialized);
    }

    public List<Flight> getAllFlights() {
        try {
            return loadFlights();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean existsById(String id) {
        for (Flight f : getAllFlights()) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPassengerToFlight(String flightId, long passengerId) {
        try {
            List<Flight> flights = loadFlights();
            boolean found = false;
            for (Flight f : flights) {
                if (f.getId().equals(flightId)) {
                    Passenger p = new PassengerStorage().getById(passengerId);
                    if (p == null) {
                        return false;
                    }
                    f.addPassenger(p);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
            saveAll(flights);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delayFlight(String flightId, int hours, int minutes) {
        try {
            List<Flight> flights = loadFlights();
            boolean found = false;
            for (Flight f : flights) {
                if (f.getId().equals(flightId)) {
                    f.delay(hours, minutes);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
            saveAll(flights);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
