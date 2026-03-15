package com.example.aviationservice.service;

import com.example.aviationservice.model.Aircraft;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AircraftService {

    private final Map<Long, Aircraft> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Aircraft create(Aircraft aircraft) {
        Aircraft entity = new Aircraft(null, aircraft.getModel(), aircraft.getRegistrationCode(), aircraft.getCapacity());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Aircraft> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Aircraft> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Aircraft> update(Long id, Aircraft aircraft) {
        Aircraft existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setModel(aircraft.getModel());
        existing.setRegistrationCode(aircraft.getRegistrationCode());
        existing.setCapacity(aircraft.getCapacity());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
