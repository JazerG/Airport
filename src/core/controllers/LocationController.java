/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Location;
import core.models.storage.LocationStorage;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author jazer
 */
public class LocationController {
    public static Response addLocation(String id, String name,String city, String country, Double latitude,Double longitude) {
        
        
        
        
        Location newLocation = new Location(id, name, city, country, latitude, longitude);

        LocationStorage storage = new LocationStorage();
        boolean saved = storage.addLocation(newLocation);

        if (!saved) {
            return new Response("Location with this ID already exists.", Status.BAD_REQUEST);
        }

        return new Response("Location successfully added", Status.OK, newLocation);
    }
    public static List<Location> getLocations() {
        return LocationStorage.getAllLocations();
    }
}
