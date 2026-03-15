package com.example.aviationservice.model;

public class Booking {
    private Long id;
    private Long passengerId;
    private Long flightId;
    private String seatNumber;

    public Booking() {
    }

    public Booking(Long id, Long passengerId, Long flightId, String seatNumber) {
        this.id = id;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
