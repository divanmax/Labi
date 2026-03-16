package com.example.aviationservice.controller;

import com.example.aviationservice.dto.AvailableSeatsResponse;
import com.example.aviationservice.model.Flight;
import com.example.aviationservice.service.AviationOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationsController {

    private final AviationOperationsService operationsService;

    public OperationsController(AviationOperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @GetMapping("/flights-by-route")
    public List<Flight> findFlightsByRoute(
            @RequestParam String departure,
            @RequestParam String arrival) {
        return operationsService.findFlightsByRoute(departure, arrival);
    }

    @GetMapping("/flights/{flightId}/available-seats")
    public ResponseEntity<AvailableSeatsResponse> getAvailableSeats(@PathVariable Long flightId) {
        try {
            return ResponseEntity.ok(operationsService.getAvailableSeats(flightId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/flights/{flightId}/cancel")
    public ResponseEntity<?> cancelFlight(@PathVariable Long flightId) {
        try {
            operationsService.cancelFlight(flightId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
