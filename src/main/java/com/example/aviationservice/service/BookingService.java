package com.example.aviationservice.service;

import com.example.aviationservice.entity.BookingEntity;
import com.example.aviationservice.entity.FlightEntity;
import com.example.aviationservice.model.Booking;
import com.example.aviationservice.model.Flight;
import com.example.aviationservice.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookingService {

    private final BookingRepository repository;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final AircraftService aircraftService;

    public BookingService(BookingRepository repository, PassengerService passengerService,
                          FlightService flightService, AircraftService aircraftService) {
        this.repository = repository;
        this.passengerService = passengerService;
        this.flightService = flightService;
        this.aircraftService = aircraftService;
    }

    @Transactional
    public Booking create(Booking booking) {
        if (booking == null) throw new IllegalArgumentException("Request body is required");
        if (booking.getPassengerId() == null || !passengerService.exists(booking.getPassengerId()))
            throw new IllegalArgumentException("Passenger not found");
        if (booking.getFlightId() == null || !flightService.exists(booking.getFlightId()))
            throw new IllegalArgumentException("Flight not found");
        FlightEntity flight = flightService.getEntityById(booking.getFlightId());
        if (flight == null) throw new IllegalArgumentException("Flight not found");
        if (!allowsNewBooking(flight.getStatus()))
            throw new IllegalArgumentException("Flight status does not allow new bookings");
        int capacity = flight.getAircraft() != null ? aircraftService.getEntityById(flight.getAircraft().getId()).getCapacity() : 0;
        long bookedCount = repository.countByFlight_Id(booking.getFlightId());
        if (bookedCount >= capacity)
            throw new IllegalArgumentException("Flight has no available seats (capacity " + capacity + ")");
        if (booking.getSeatNumber() != null && !booking.getSeatNumber().isBlank()) {
            if (repository.existsByFlight_IdAndSeatNumber(booking.getFlightId(), booking.getSeatNumber()))
                throw new IllegalArgumentException("Seat " + booking.getSeatNumber() + " is already taken");
        }
        BookingEntity e = new BookingEntity();
        e.setPassenger(passengerService.getEntityById(booking.getPassengerId()));
        e.setFlight(flight);
        e.setSeatNumber(booking.getSeatNumber());
        e = repository.save(e);
        return toModel(e);
    }

    public Optional<Booking> getById(Long id) {
        return repository.findById(id).map(BookingService::toModel);
    }

    public List<Booking> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(BookingService::toModel).toList();
    }

    public List<Booking> getByFlightId(Long flightId) {
        return repository.findByFlight_Id(flightId).stream().map(BookingService::toModel).toList();
    }

    public List<Booking> getByPassengerId(Long passengerId) {
        return repository.findByPassenger_Id(passengerId).stream().map(BookingService::toModel).toList();
    }

    @Transactional
    public Optional<Booking> update(Long id, Booking booking) {
        if (booking == null) throw new IllegalArgumentException("Request body is required");
        return repository.findById(id).map(e -> {
            if (booking.getPassengerId() != null) e.setPassenger(passengerService.getEntityById(booking.getPassengerId()));
            if (booking.getFlightId() != null) e.setFlight(flightService.getEntityById(booking.getFlightId()));
            e.setSeatNumber(booking.getSeatNumber());
            return BookingService.toModel(repository.save(e));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        BookingEntity b = repository.findById(id).orElse(null);
        if (b == null) return false;
        if (!allowsRefund(b.getFlight().getStatus()))
            throw new IllegalArgumentException("Flight status does not allow refund/cancellation");
        repository.deleteById(id);
        return true;
    }

    private static boolean allowsNewBooking(String status) {
        return Flight.STATUS_SCHEDULED.equals(status) || Flight.STATUS_BOARDING.equals(status);
    }

    private static boolean allowsRefund(String status) {
        return Flight.STATUS_SCHEDULED.equals(status) || Flight.STATUS_BOARDING.equals(status);
    }

    private static Booking toModel(BookingEntity e) {
        Booking m = new Booking();
        m.setId(e.getId());
        m.setPassengerId(e.getPassenger() != null ? e.getPassenger().getId() : null);
        m.setFlightId(e.getFlight() != null ? e.getFlight().getId() : null);
        m.setSeatNumber(e.getSeatNumber());
        return m;
    }
}
