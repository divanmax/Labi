package com.example.aviationservice.repository;

import com.example.aviationservice.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

    @Query("SELECT f FROM FlightEntity f JOIN FETCH f.departureAirport JOIN FETCH f.arrivalAirport " +
           "WHERE f.departureAirport.code = :depCode AND f.arrivalAirport.code = :arrCode")
    List<FlightEntity> findByRoute(@Param("depCode") String departureCode, @Param("arrCode") String arrivalCode);
}
