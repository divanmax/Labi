package com.example.aviationservice.repository;

import com.example.aviationservice.entity.SessionStatus;
import com.example.aviationservice.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {

    Optional<UserSessionEntity> findByRefreshTokenIdAndStatus(String refreshTokenId, SessionStatus status);
}

