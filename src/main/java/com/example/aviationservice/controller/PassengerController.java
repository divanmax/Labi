package com.example.aviationservice.controller;

import com.example.aviationservice.model.Passenger;
import com.example.aviationservice.service.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public ResponseEntity<Passenger> create(@RequestBody Passenger passenger) {
        Passenger created = passengerService.create(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getById(@PathVariable Long id) {
        return passengerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Passenger> getAll() {
        return passengerService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> update(@PathVariable Long id, @RequestBody Passenger passenger) {
        return passengerService.update(id, passenger)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!passengerService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
