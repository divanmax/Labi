package com.example.aviationservice.controller;

import com.example.aviationservice.model.Aircraft;
import com.example.aviationservice.service.AircraftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping
    public ResponseEntity<Aircraft> create(@RequestBody Aircraft aircraft) {
        Aircraft created = aircraftService.create(aircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getById(@PathVariable Long id) {
        return aircraftService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Aircraft> getAll() {
        return aircraftService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> update(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        return aircraftService.update(id, aircraft)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!aircraftService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
