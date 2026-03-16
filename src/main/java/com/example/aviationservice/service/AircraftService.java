package com.example.aviationservice.service;

import com.example.aviationservice.entity.AircraftEntity;
import com.example.aviationservice.model.Aircraft;
import com.example.aviationservice.repository.AircraftRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AircraftService {

    private final AircraftRepository repository;

    public AircraftService(AircraftRepository repository) {
        this.repository = repository;
    }

    public Aircraft create(Aircraft aircraft) {
        if (aircraft == null) throw new IllegalArgumentException("Request body is required");
        if (aircraft.getCapacity() < 1) throw new IllegalArgumentException("Capacity must be at least 1");
        AircraftEntity e = new AircraftEntity();
        e.setModel(aircraft.getModel());
        e.setRegistrationCode(aircraft.getRegistrationCode());
        e.setCapacity(aircraft.getCapacity());
        e = repository.save(e);
        return toModel(e);
    }

    public Optional<Aircraft> getById(Long id) {
        return repository.findById(id).map(AircraftService::toModel);
    }

    public List<Aircraft> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(AircraftService::toModel).toList();
    }

    public Optional<Aircraft> update(Long id, Aircraft aircraft) {
        if (aircraft == null) throw new IllegalArgumentException("Request body is required");
        if (aircraft.getCapacity() < 1) throw new IllegalArgumentException("Capacity must be at least 1");
        return repository.findById(id).map(e -> {
            e.setModel(aircraft.getModel());
            e.setRegistrationCode(aircraft.getRegistrationCode());
            e.setCapacity(aircraft.getCapacity());
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

    AircraftEntity getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    private static Aircraft toModel(AircraftEntity e) {
        Aircraft m = new Aircraft();
        m.setId(e.getId());
        m.setModel(e.getModel());
        m.setRegistrationCode(e.getRegistrationCode());
        m.setCapacity(e.getCapacity());
        return m;
    }
}
