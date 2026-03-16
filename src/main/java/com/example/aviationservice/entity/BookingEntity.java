package com.example.aviationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "booking",
       uniqueConstraints = @UniqueConstraint(name = "uk_booking_flight_seat", columnNames = { "flight_id", "seat_number" }))
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    @NotNull
    private PassengerEntity passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @NotNull
    private FlightEntity flight;

    @Column(name = "seat_number", length = 10)
    private String seatNumber;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PassengerEntity getPassenger() { return passenger; }
    public void setPassenger(PassengerEntity passenger) { this.passenger = passenger; }
    public FlightEntity getFlight() { return flight; }
    public void setFlight(FlightEntity flight) { this.flight = flight; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
}
