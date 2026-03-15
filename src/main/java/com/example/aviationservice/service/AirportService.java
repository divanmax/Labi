package com.example.aviationservice.service;

import com.example.aviationservice.model.Airport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AirportService {

    private final Map<Long, Airport> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Airport create(Airport airport) {
        Airport entity = new Airport(null, airport.getCode(), airport.getName(), airport.getCity());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Airport> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Airport> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Airport> update(Long id, Airport airport) {
        Airport existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setCode(airport.getCode());
        existing.setName(airport.getName());
        existing.setCity(airport.getCity());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
