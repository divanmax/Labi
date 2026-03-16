package com.example.aviationservice.repository;

import com.example.aviationservice.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
    Optional<AirportEntity> findByCode(String code);
}
