package com.example.aviationservice.model;

import java.time.Instant;

public class Flight {
    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_BOARDING = "BOARDING";
    public static final String STATUS_DEPARTED = "DEPARTED";
    public static final String STATUS_ARRIVED = "ARRIVED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private Long id;
    private Long aircraftId;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private Instant departureTime;
    private Instant arrivalTime;
    private String status = STATUS_SCHEDULED;

    public Flight() {
    }

    public Flight(Long id, Long aircraftId, Long departureAirportId, Long arrivalAirportId,
                  Instant departureTime, Instant arrivalTime, String status) {
        this.id = id;
        this.aircraftId = aircraftId;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status != null ? status : STATUS_SCHEDULED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public Long getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(Long departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public Long getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(Long arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
