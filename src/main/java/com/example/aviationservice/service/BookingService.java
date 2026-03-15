package com.example.aviationservice.service;

import com.example.aviationservice.model.Booking;
import com.example.aviationservice.model.Flight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookingService {

    private final Map<Long, Booking> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    private final PassengerService passengerService;
    private final FlightService flightService;
    private final AircraftService aircraftService;

    public BookingService(PassengerService passengerService, FlightService flightService, AircraftService aircraftService) {
        this.passengerService = passengerService;
        this.flightService = flightService;
        this.aircraftService = aircraftService;
    }

    public Booking create(Booking booking) {
        if (booking.getPassengerId() == null || !passengerService.exists(booking.getPassengerId())) {
            throw new IllegalArgumentException("Passenger with id " + booking.getPassengerId() + " not found");
        }
        if (booking.getFlightId() == null || !flightService.exists(booking.getFlightId())) {
            throw new IllegalArgumentException("Flight with id " + booking.getFlightId() + " not found");
        }
        Flight flight = flightService.getById(booking.getFlightId()).orElseThrow();
        if (!allowsNewBooking(flight.getStatus())) {
            throw new IllegalArgumentException("Flight status " + flight.getStatus() + " does not allow new bookings");
        }
        int capacity = flight.getAircraftId() != null ? aircraftService.getById(flight.getAircraftId()).map(a -> a.getCapacity()).orElse(0) : 0;
        long bookedCount = storage.values().stream().filter(b -> booking.getFlightId().equals(b.getFlightId())).count();
        if (bookedCount >= capacity) {
            throw new IllegalArgumentException("Flight has no available seats (capacity " + capacity + ")");
        }
        if (booking.getSeatNumber() != null && !booking.getSeatNumber().isBlank()) {
            boolean seatTaken = storage.values().stream()
                    .anyMatch(b -> booking.getFlightId().equals(b.getFlightId()) && booking.getSeatNumber().equals(b.getSeatNumber()));
            if (seatTaken) {
                throw new IllegalArgumentException("Seat " + booking.getSeatNumber() + " is already taken on this flight");
            }
        }
        Booking entity = new Booking(null, booking.getPassengerId(), booking.getFlightId(), booking.getSeatNumber());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Booking> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Booking> getAll() {
        return List.copyOf(storage.values());
    }

    public List<Booking> getByFlightId(Long flightId) {
        return storage.values().stream().filter(b -> flightId.equals(b.getFlightId())).toList();
    }

    public List<Booking> getByPassengerId(Long passengerId) {
        return storage.values().stream().filter(b -> passengerId.equals(b.getPassengerId())).toList();
    }

    public Optional<Booking> update(Long id, Booking booking) {
        Booking existing = storage.get(id);
        if (existing == null) return Optional.empty();
        if (booking.getPassengerId() != null && !passengerService.exists(booking.getPassengerId())) {
            throw new IllegalArgumentException("Passenger with id " + booking.getPassengerId() + " not found");
        }
        if (booking.getFlightId() != null && !flightService.exists(booking.getFlightId())) {
            throw new IllegalArgumentException("Flight with id " + booking.getFlightId() + " not found");
        }
        existing.setPassengerId(booking.getPassengerId());
        existing.setFlightId(booking.getFlightId());
        existing.setSeatNumber(booking.getSeatNumber());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        Booking booking = storage.get(id);
        if (booking == null) return false;
        Optional<Flight> flightOpt = flightService.getById(booking.getFlightId());
        if (flightOpt.isPresent() && !allowsRefund(flightOpt.get().getStatus())) {
            throw new IllegalArgumentException("Flight status " + flightOpt.get().getStatus() + " does not allow refund/cancellation");
        }
        return storage.remove(id) != null;
    }

    private static boolean allowsNewBooking(String status) {
        return Flight.STATUS_SCHEDULED.equals(status) || Flight.STATUS_BOARDING.equals(status);
    }

    private static boolean allowsRefund(String status) {
        return Flight.STATUS_SCHEDULED.equals(status) || Flight.STATUS_BOARDING.equals(status);
    }
}
