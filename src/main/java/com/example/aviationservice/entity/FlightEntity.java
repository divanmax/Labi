package com.example.aviationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(name = "flight")
public class FlightEntity {

    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_BOARDING = "BOARDING";
    public static final String STATUS_DEPARTED = "DEPARTED";
    public static final String STATUS_ARRIVED = "ARRIVED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    @NotNull
    private AirportEntity departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    @NotNull
    private AirportEntity arrivalAirport;

    @Column(name = "departure_time")
    private Instant departureTime;

    @Column(name = "arrival_time")
    private Instant arrivalTime;

    @Column(nullable = false, length = 20)
    private String status = STATUS_SCHEDULED;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AircraftEntity getAircraft() { return aircraft; }
    public void setAircraft(AircraftEntity aircraft) { this.aircraft = aircraft; }
    public AirportEntity getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(AirportEntity departureAirport) { this.departureAirport = departureAirport; }
    public AirportEntity getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(AirportEntity arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public Instant getDepartureTime() { return departureTime; }
    public void setDepartureTime(Instant departureTime) { this.departureTime = departureTime; }
    public Instant getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Instant arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
