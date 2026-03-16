package com.example.aviationservice.service;

import com.example.aviationservice.dto.AvailableSeatsResponse;
import com.example.aviationservice.entity.FlightEntity;
import com.example.aviationservice.model.Flight;
import com.example.aviationservice.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AviationOperationsService {

    private final FlightService flightService;
    private final AircraftService aircraftService;
    private final BookingRepository bookingRepository;
    private final com.example.aviationservice.repository.FlightRepository flightRepository;

    public AviationOperationsService(FlightService flightService, AircraftService aircraftService,
                                     BookingRepository bookingRepository,
                                     com.example.aviationservice.repository.FlightRepository flightRepository) {
        this.flightService = flightService;
        this.aircraftService = aircraftService;
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public List<Flight> findFlightsByRoute(String departureAirportCode, String arrivalAirportCode) {
        return flightService.findByRoute(departureAirportCode, arrivalAirportCode);
    }

    public AvailableSeatsResponse getAvailableSeats(Long flightId) {
        Flight flight = flightService.getById(flightId).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
        int capacity = flight.getAircraftId() != null
                ? aircraftService.getById(flight.getAircraftId()).map(a -> a.getCapacity()).orElse(0)
                : 0;
        long bookedCount = bookingRepository.countByFlight_Id(flightId);
        List<String> bookedSeats = bookingRepository.findByFlight_Id(flightId).stream()
                .map(b -> b.getSeatNumber())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.toList());
        return new AvailableSeatsResponse(flightId, capacity, bookedCount, bookedSeats);
    }

    @Transactional
    public void cancelFlight(Long flightId) {
        FlightEntity flight = flightRepository.findById(flightId).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
        bookingRepository.findByFlight_Id(flightId).forEach(bookingRepository::delete);
        flight.setStatus(FlightEntity.STATUS_CANCELLED);
        flightRepository.save(flight);
    }
}
