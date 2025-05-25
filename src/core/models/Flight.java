package core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import core.models.storage.PassengerStorage;

/**
 *
 * @author edangulo
 */
public class Flight {

    private final String id;
    private ArrayList<Passenger> passengers;
    private Plane plane;
    private Location departureLocation;
    private Location scaleLocation;
    private Location arrivalLocation;
    private LocalDateTime departureDate;
    private int hoursDurationArrival;
    private int minutesDurationArrival;
    private int hoursDurationScale;
    private int minutesDurationScale;

    public Flight(String id,
            Plane plane,
            Location departureLocation,
            Location arrivalLocation,
            LocalDateTime departureDate,
            int hoursDurationArrival,
            int minutesDurationArrival) {
        this.id = id;
        this.passengers = new ArrayList<>();
        this.plane = plane;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
        this.hoursDurationArrival = hoursDurationArrival;
        this.minutesDurationArrival = minutesDurationArrival;

        this.plane.addFlight(this);
    }

    public Flight(String id,
            Plane plane,
            Location departureLocation,
            Location scaleLocation,
            Location arrivalLocation,
            LocalDateTime departureDate,
            int hoursDurationArrival,
            int minutesDurationArrival,
            int hoursDurationScale,
            int minutesDurationScale) {
        this.id = id;
        this.passengers = new ArrayList<>();
        this.plane = plane;
        this.departureLocation = departureLocation;
        this.scaleLocation = scaleLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
        this.hoursDurationArrival = hoursDurationArrival;
        this.minutesDurationArrival = minutesDurationArrival;
        this.hoursDurationScale = hoursDurationScale;
        this.minutesDurationScale = minutesDurationScale;

        this.plane.addFlight(this);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("planeId", plane.getId());
        obj.put("departureId", departureLocation.getAirportId());
        obj.put("arrivalId", arrivalLocation.getAirportId());
        obj.put("scaleId", scaleLocation != null ? scaleLocation.getAirportId() : JSONObject.NULL);
        obj.put("departureDate", departureDate.toString());
        obj.put("hoursDurationArrival", hoursDurationArrival);
        obj.put("minutesDurationArrival", minutesDurationArrival);
        obj.put("hoursDurationScale", hoursDurationScale);
        obj.put("minutesDurationScale", minutesDurationScale);

        // Agregar lista de IDs de pasajeros
        JSONArray paxArray = new JSONArray();
        for (Passenger p : passengers) {
            paxArray.put(p.getId());
        }
        obj.put("passengers", paxArray);

        return obj;
    }

    public static Flight fromJson(JSONObject obj) {
        String id = obj.getString("id");
        String planeId = obj.getString("planeId");
        String departureId = obj.getString("departureId");
        String arrivalId = obj.getString("arrivalId");
        String scaleId = obj.isNull("scaleId") ? null : obj.getString("scaleId");
        LocalDateTime departureDate
                = LocalDateTime.parse(obj.getString("departureDate"));

        int arrivalHours = obj.getInt("hoursDurationArrival");
        int arrivalMinutes = obj.getInt("minutesDurationArrival");
        int scaleHours = obj.getInt("hoursDurationScale");
        int scaleMinutes = obj.getInt("minutesDurationScale");

        // Usar los Storage para reconstruir referencias
        Plane plane = new core.models.storage.PlaneStorage().getById(planeId);
        Location departure = new core.models.storage.LocationStorage().getById(departureId);
        Location arrival = new core.models.storage.LocationStorage().getById(arrivalId);
        Location scale = (scaleId != null)
                ? new core.models.storage.LocationStorage().getById(scaleId)
                : null;

        Flight flight;
        if (scale != null) {
            flight = new Flight(id, plane, departure, scale, arrival,
                    departureDate, arrivalHours, arrivalMinutes,
                    scaleHours, scaleMinutes);
        } else {
            flight = new Flight(id, plane, departure, arrival,
                    departureDate, arrivalHours, arrivalMinutes);
        }

        // Cargar pasajeros desde JSON
        JSONArray paxArray = obj.optJSONArray("passengers");
        if (paxArray != null) {
            PassengerStorage pst = new PassengerStorage();
            for (int i = 0; i < paxArray.length(); i++) {
                long pid = paxArray.getLong(i);
                Passenger p = pst.getById(pid);
                if (p != null) {
                    flight.addPassenger(p);
                }
            }
        }

        return flight;
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
    }

    public String getId() {
        return id;
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public Location getScaleLocation() {
        return scaleLocation;
    }

    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public int getHoursDurationArrival() {
        return hoursDurationArrival;
    }

    public int getMinutesDurationArrival() {
        return minutesDurationArrival;
    }

    public int getHoursDurationScale() {
        return hoursDurationScale;
    }

    public int getMinutesDurationScale() {
        return minutesDurationScale;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime calculateArrivalDate() {
        return departureDate.plusHours(hoursDurationScale).plusHours(hoursDurationArrival).plusMinutes(minutesDurationScale).plusMinutes(minutesDurationArrival);
    }

    public void delay(int hours, int minutes) {
        this.departureDate = this.departureDate.plusHours(hours).plusMinutes(minutes);
    }

    public int getNumPassengers() {
        return passengers.size();
    }
}
