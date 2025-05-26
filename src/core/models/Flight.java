package core.models;

import core.models.storage.LocationStorage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import core.models.storage.PassengerStorage;
import core.models.storage.PlaneStorage;

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
        obj.put("plane", plane.getId());
        obj.put("departureLocation", departureLocation.getAirportId());
        obj.put("arrivalLocation", arrivalLocation.getAirportId());
        obj.put("scaleLocation",scaleLocation != null? scaleLocation.getAirportId(): JSONObject.NULL);
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
        String planeId = obj.getString("plane");
        String depLocId = obj.getString("departureLocation");
        String arrLocId = obj.getString("arrivalLocation");
        String scaleLocId = obj.isNull("scaleLocation") ? null : obj.getString("scaleLocation");
        LocalDateTime depDt = LocalDateTime.parse(obj.getString("departureDate"));

        int arrHrs = obj.getInt("hoursDurationArrival");
        int arrMin = obj.getInt("minutesDurationArrival");
        int sclHrs = obj.getInt("hoursDurationScale");
        int sclMin = obj.getInt("minutesDurationScale");

        // Reconstruir referencias
        Plane plane = new PlaneStorage().getById(planeId);
        Location departure = new LocationStorage().getById(depLocId);
        Location arrival = new LocationStorage().getById(arrLocId);
        Location scale = (scaleLocId != null) ? new LocationStorage().getById(scaleLocId): null;

        Flight flight;
        if (scale != null) {
            flight = new Flight(
                    id, plane, departure, scale, arrival,
                    depDt, arrHrs, arrMin,
                    sclHrs, sclMin
            );
        } else {
            flight = new Flight(
                    id, plane, departure, arrival,
                    depDt, arrHrs, arrMin
            );
        }

        JSONArray paxArray2 = obj.optJSONArray("passengers");
        if (paxArray2 != null) {
            PassengerStorage pst = new PassengerStorage();
            for (int i = 0; i < paxArray2.length(); i++) {
                long pid = paxArray2.getLong(i);
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
