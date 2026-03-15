package com.example.aviationservice.service;

import com.example.aviationservice.model.Flight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FlightService {

    private final Map<Long, Flight> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    private final AircraftService aircraftService;
    private final AirportService airportService;

    public FlightService(AircraftService aircraftService, AirportService airportService) {
        this.aircraftService = aircraftService;
        this.airportService = airportService;
    }

    public Flight create(Flight flight) {
        if (flight.getAircraftId() != null && !aircraftService.exists(flight.getAircraftId())) {
            throw new IllegalArgumentException("Aircraft with id " + flight.getAircraftId() + " not found");
        }
        if (flight.getDepartureAirportId() != null && !airportService.exists(flight.getDepartureAirportId())) {
            throw new IllegalArgumentException("Departure airport with id " + flight.getDepartureAirportId() + " not found");
        }
        if (flight.getArrivalAirportId() != null && !airportService.exists(flight.getArrivalAirportId())) {
            throw new IllegalArgumentException("Arrival airport with id " + flight.getArrivalAirportId() + " not found");
        }
        Flight entity = new Flight(null, flight.getAircraftId(), flight.getDepartureAirportId(), flight.getArrivalAirportId(),
                flight.getDepartureTime(), flight.getArrivalTime(), flight.getStatus());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Flight> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Flight> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Flight> update(Long id, Flight flight) {
        Flight existing = storage.get(id);
        if (existing == null) return Optional.empty();
        if (flight.getAircraftId() != null && !aircraftService.exists(flight.getAircraftId())) {
            throw new IllegalArgumentException("Aircraft with id " + flight.getAircraftId() + " not found");
        }
        if (flight.getDepartureAirportId() != null && !airportService.exists(flight.getDepartureAirportId())) {
            throw new IllegalArgumentException("Departure airport with id " + flight.getDepartureAirportId() + " not found");
        }
        if (flight.getArrivalAirportId() != null && !airportService.exists(flight.getArrivalAirportId())) {
            throw new IllegalArgumentException("Arrival airport with id " + flight.getArrivalAirportId() + " not found");
        }
        existing.setAircraftId(flight.getAircraftId());
        existing.setDepartureAirportId(flight.getDepartureAirportId());
        existing.setArrivalAirportId(flight.getArrivalAirportId());
        existing.setDepartureTime(flight.getDepartureTime());
        existing.setArrivalTime(flight.getArrivalTime());
        existing.setStatus(flight.getStatus());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
