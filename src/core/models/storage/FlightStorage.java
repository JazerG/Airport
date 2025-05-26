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
import java.util.ArrayList;
import java.util.List;

public class FlightStorage {

    private static final String FILE_PATH = "json/flights.json";

    public void saveFlight(Flight flight) throws IOException {
        List<Flight> flights = loadFlights();
        for (Flight f : flights) {
            if (f.getId().equals(flight.getId())) {
                throw new IllegalArgumentException("Ya existe un vuelo con ese ID.");
            }
        }
        flights.add(flight);
        saveAll(flights);
    }

    private List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return flights;
        }

        try {
            String content = Files.readString(file.toPath());
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                flights.add(Flight.fromJson(array.getJSONObject(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flights;
    }

    private void saveAll(List<Flight> flights) throws IOException {
        JSONArray array = new JSONArray();
        for (Flight f : flights) {
            array.put(f.toJson());
        }
        try ( FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(array.toString(4));
        }
    }

    public List<Flight> getAllFlights() {
        return loadFlights();
    }

    public boolean existsById(String id) {
        for (Flight f : loadFlights()) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPassengerToFlight(String flightId, long passengerId) {
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

        try {
            saveAll(flights);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delayFlight(String flightId, int hours, int minutes) {
    List<Flight> flights = loadFlights();
    boolean found = false;
    for (Flight f : flights) {
        if (f.getId().equals(flightId)) {
            f.delay(hours, minutes);
            found = true;
            break;
        }
    }
    if (!found) return false;
    try {
        saveAll(flights);
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}
}
