package com.example.aviationservice.service;

import com.example.aviationservice.entity.PassengerEntity;
import com.example.aviationservice.model.Passenger;
import com.example.aviationservice.repository.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PassengerService {

    private final PassengerRepository repository;

    public PassengerService(PassengerRepository repository) {
        this.repository = repository;
    }

    public Passenger create(Passenger passenger) {
        if (passenger == null) throw new IllegalArgumentException("Request body is required");
        PassengerEntity e = new PassengerEntity();
        e.setFirstName(passenger.getFirstName());
        e.setLastName(passenger.getLastName());
        e.setEmail(passenger.getEmail());
        e.setPassportNumber(passenger.getPassportNumber());
        e = repository.save(e);
        return toModel(e);
    }

    public Optional<Passenger> getById(Long id) {
        return repository.findById(id).map(PassengerService::toModel);
    }

    public List<Passenger> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(PassengerService::toModel).toList();
    }

    public Optional<Passenger> update(Long id, Passenger passenger) {
        if (passenger == null) throw new IllegalArgumentException("Request body is required");
        return repository.findById(id).map(e -> {
            e.setFirstName(passenger.getFirstName());
            e.setLastName(passenger.getLastName());
            e.setEmail(passenger.getEmail());
            e.setPassportNumber(passenger.getPassportNumber());
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

    PassengerEntity getEntityById(Long id) {
        return repository.findById(id).orElse(null);
    }

    private static Passenger toModel(PassengerEntity e) {
        Passenger m = new Passenger();
        m.setId(e.getId());
        m.setFirstName(e.getFirstName());
        m.setLastName(e.getLastName());
        m.setEmail(e.getEmail());
        m.setPassportNumber(e.getPassportNumber());
        return m;
    }
}
