package com.example.aviationservice.repository;

import com.example.aviationservice.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {
}
