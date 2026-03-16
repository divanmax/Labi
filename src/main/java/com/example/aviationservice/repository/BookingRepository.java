package com.example.aviationservice.repository;

import com.example.aviationservice.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByFlight_Id(Long flightId);

    List<BookingEntity> findByPassenger_Id(Long passengerId);

    long countByFlight_Id(Long flightId);

    boolean existsByFlight_IdAndSeatNumber(Long flightId, String seatNumber);
}
