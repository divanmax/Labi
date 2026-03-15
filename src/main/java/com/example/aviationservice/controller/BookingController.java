package com.example.aviationservice.controller;

import com.example.aviationservice.model.Booking;
import com.example.aviationservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Booking booking) {
        try {
            Booking created = bookingService.create(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return bookingService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Booking> getAll(@RequestParam(required = false) Long flightId, @RequestParam(required = false) Long passengerId) {
        if (flightId != null) {
            return bookingService.getByFlightId(flightId);
        }
        if (passengerId != null) {
            return bookingService.getByPassengerId(passengerId);
        }
        return bookingService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            return bookingService.update(id, booking)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (!bookingService.delete(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
