package com.example.aviationservice.service;

import com.example.aviationservice.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PassengerService {

    private final Map<Long, Passenger> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Passenger create(Passenger passenger) {
        Passenger entity = new Passenger(null, passenger.getFirstName(), passenger.getLastName(), passenger.getEmail(), passenger.getPassportNumber());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Passenger> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Passenger> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Passenger> update(Long id, Passenger passenger) {
        Passenger existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setFirstName(passenger.getFirstName());
        existing.setLastName(passenger.getLastName());
        existing.setEmail(passenger.getEmail());
        existing.setPassportNumber(passenger.getPassportNumber());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
