/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.utils;

import core.models.Flight;

/**
 *
 * @author jazer
 */
public class FlightResponse {
    private int statusCode;
    private String message;
    private Flight data;

    public FlightResponse(int statusCode, String message, Flight data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public Flight getData() { return data; }
}
