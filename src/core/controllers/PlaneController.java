/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Plane;
import core.models.storage.PlaneStorage;
import java.util.List;



/**
 *
 * @author jazer
 */
public class PlaneController {
    public static Response addPlane(String id,String brand,String model,int maxCapacity, String airline) {
        

               
        Plane newPlane = new Plane(id, brand, model, maxCapacity, airline);

        PlaneStorage storage = new PlaneStorage();
        boolean saved = storage.addPlane(newPlane);

        if (!saved) {
            return new Response("Location with this ID already exists.", Status.BAD_REQUEST);
        }

        return new Response("Location successfully added", Status.OK, newPlane);
    }
    public static List<Plane> getPlanes() {
        return PlaneStorage.getAllPlanes();
    }
}
