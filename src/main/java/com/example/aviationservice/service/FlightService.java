package com.example.aviationservice.service;

import com.example.aviationservice.entity.AircraftEntity;
import com.example.aviationservice.entity.AirportEntity;
import com.example.aviationservice.entity.FlightEntity;
import com.example.aviationservice.model.Flight;
import com.example.aviationservice.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FlightService {

    private final FlightRepository repository;
    private final AircraftService aircraftService;
    private final AirportService airportService;

    public FlightService(FlightRepository repository, AircraftService aircraftService, AirportService airportService) {
        this.repository = repository;
        this.aircraftService = aircraftService;
        this.airportService = airportService;
    }

    public Flight create(Flight flight) {
        if (flight == null) throw new IllegalArgumentException("Request body is required");
        if (flight.getAircraftId() != null && !aircraftService.exists(flight.getAircraftId()))
            throw new IllegalArgumentException("Aircraft with id " + flight.getAircraftId() + " not found");
        if (flight.getDepartureAirportId() == null || !airportService.exists(flight.getDepartureAirportId()))
            throw new IllegalArgumentException("Departure airport not found");
        if (flight.getArrivalAirportId() == null || !airportService.exists(flight.getArrivalAirportId()))
            throw new IllegalArgumentException("Arrival airport not found");
        FlightEntity e = new FlightEntity();
        e.setAircraft(flight.getAircraftId() != null ? aircraftService.getEntityById(flight.getAircraftId()) : null);
        e.setDepartureAirport(airportService.getEntityById(flight.getDepartureAirportId()));
        e.setArrivalAirport(airportService.getEntityById(flight.getArrivalAirportId()));
        e.setDepartureTime(flight.getDepartureTime());
        e.setArrivalTime(flight.getArrivalTime());
        e.setStatus(flight.getStatus() != null ? flight.getStatus() : FlightEntity.STATUS_SCHEDULED);
        e = repository.save(e);
        return toModel(e);
    }

    public Optional<Flight> getById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    public List<Flight> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(this::toModel).toList();
    }

    public Optional<Flight> update(Long id, Flight flight) {
        if (flight == null) throw new IllegalArgumentException("Request body is required");
        return repository.findById(id).map(e -> {
            if (flight.getAircraftId() != null && !aircraftService.exists(flight.getAircraftId()))
                throw new IllegalArgumentException("Aircraft with id " + flight.getAircraftId() + " not found");
            if (flight.getDepartureAirportId() != null) e.setDepartureAirport(airportService.getEntityById(flight.getDepartureAirportId()));
            if (flight.getArrivalAirportId() != null) e.setArrivalAirport(airportService.getEntityById(flight.getArrivalAirportId()));
            e.setAircraft(flight.getAircraftId() != null ? aircraftService.getEntityById(flight.getAircraftId()) : null);
            e.setDepartureTime(flight.getDepartureTime());
            e.setArrivalTime(flight.getArrivalTime());
            if (flight.getStatus() != null) e.setStatus(flight.getStatus());
            return toModel(repository.save(e));
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    FlightEntity getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Flight> findByRoute(String departureCode, String arrivalCode) {
        return repository.findByRoute(departureCode, arrivalCode).stream().map(this::toModel).toList();
    }

    private Flight toModel(FlightEntity e) {
        Flight m = new Flight();
        m.setId(e.getId());
        m.setAircraftId(e.getAircraft() != null ? e.getAircraft().getId() : null);
        m.setDepartureAirportId(e.getDepartureAirport() != null ? e.getDepartureAirport().getId() : null);
        m.setArrivalAirportId(e.getArrivalAirport() != null ? e.getArrivalAirport().getId() : null);
        m.setDepartureTime(e.getDepartureTime());
        m.setArrivalTime(e.getArrivalTime());
        m.setStatus(e.getStatus());
        return m;
    }
}
