package com.example.aviationservice.service;

import com.example.aviationservice.entity.AirportEntity;
import com.example.aviationservice.model.Airport;
import com.example.aviationservice.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AirportService {

    private final AirportRepository repository;

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    public Airport create(Airport airport) {
        if (airport == null) throw new IllegalArgumentException("Request body is required");
        AirportEntity e = new AirportEntity();
        e.setCode(airport.getCode());
        e.setName(airport.getName());
        e.setCity(airport.getCity());
        e = repository.save(e);
        return toModel(e);
    }

    public Optional<Airport> getById(Long id) {
        return repository.findById(id).map(AirportService::toModel);
    }

    public List<Airport> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(AirportService::toModel).toList();
    }

    public Optional<Airport> update(Long id, Airport airport) {
        if (airport == null) throw new IllegalArgumentException("Request body is required");
        return repository.findById(id).map(e -> {
            e.setCode(airport.getCode());
            e.setName(airport.getName());
            e.setCity(airport.getCity());
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

    AirportEntity getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    Optional<AirportEntity> findByCode(String code) {
        return repository.findByCode(code);
    }

    private static Airport toModel(AirportEntity e) {
        Airport m = new Airport();
        m.setId(e.getId());
        m.setCode(e.getCode());
        m.setName(e.getName());
        m.setCity(e.getCity());
        return m;
    }
}
