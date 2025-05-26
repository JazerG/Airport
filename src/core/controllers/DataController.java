/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

/**
 *
 * @author jazer
 */
import core.models.Location;
import core.models.Plane;
import core.models.storage.LocationStorage;
import core.models.storage.PlaneStorage;
import java.util.List;
import java.util.stream.Collectors;

public class DataController {
    private PlaneStorage planeStorage = new PlaneStorage();
    private LocationStorage locationStorage = new LocationStorage();

    public List<String> getPlaneIds() {
        return planeStorage.getAllPlanes().stream()
                .map(Plane::getId)
                .collect(Collectors.toList());
    }

    public List<String> getLocationIds() {
        return locationStorage.getAllLocations().stream()
                .map(Location::getAirportId)
                .collect(Collectors.toList());
    }
}