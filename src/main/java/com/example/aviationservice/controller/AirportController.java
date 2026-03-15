package com.example.aviationservice.controller;

import com.example.aviationservice.model.Airport;
import com.example.aviationservice.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping
    public ResponseEntity<Airport> create(@RequestBody Airport airport) {
        Airport created = airportService.create(airport);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> getById(@PathVariable Long id) {
        return airportService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Airport> getAll() {
        return airportService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> update(@PathVariable Long id, @RequestBody Airport airport) {
        return airportService.update(id, airport)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!airportService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
