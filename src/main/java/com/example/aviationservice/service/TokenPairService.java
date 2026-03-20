package com.example.aviationservice.service;

import com.example.aviationservice.dto.TokenPairResponse;
import com.example.aviationservice.entity.AppUserEntity;
import com.example.aviationservice.entity.SessionStatus;
import com.example.aviationservice.entity.UserSessionEntity;
import com.example.aviationservice.repository.AppUserRepository;
import com.example.aviationservice.repository.UserSessionRepository;
import com.example.aviationservice.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TokenPairService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenPairService(AuthenticationManager authenticationManager,
                            AppUserRepository userRepository,
                            UserSessionRepository userSessionRepository,
                            JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenPairResponse login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        AppUserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String refreshTokenId = jwtTokenProvider.newRefreshTokenId();
        return issueTokenPair(user, refreshTokenId, null);
    }

    @Transactional
    public TokenPairResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token type");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String refreshTokenId = jwtTokenProvider.getRefreshTokenId(refreshToken);

        UserSessionEntity currentSession = userSessionRepository
                .findByRefreshTokenIdAndStatus(refreshTokenId, SessionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is revoked or already used"));

        if (currentSession.getExpiresAt().isBefore(Instant.now())) {
            currentSession.setStatus(SessionStatus.EXPIRED);
            userSessionRepository.save(currentSession);
            throw new IllegalArgumentException("Refresh token expired");
        }

        AppUserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newRefreshTokenId = jwtTokenProvider.newRefreshTokenId();
        return issueTokenPair(user, newRefreshTokenId, currentSession);
    }

    private TokenPairResponse issueTokenPair(AppUserEntity user, String refreshTokenId, UserSessionEntity currentSession) {
        if (currentSession != null) {
            currentSession.setStatus(SessionStatus.REFRESHED);
            userSessionRepository.save(currentSession);
        }

        Instant now = Instant.now();
        Instant refreshExp = now.plusSeconds(jwtTokenProvider.getRefreshExpirationSec());

        UserSessionEntity newSession = new UserSessionEntity();
        newSession.setUser(user);
        newSession.setRefreshTokenId(refreshTokenId);
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setExpiresAt(refreshExp);
        userSessionRepository.save(newSession);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getRole(), refreshTokenId);

        return new TokenPairResponse(
                "Bearer",
                accessToken,
                refreshToken,
                now.plusSeconds(jwtTokenProvider.getAccessExpirationSec()).getEpochSecond(),
                refreshExp.getEpochSecond()
        );
    }
}

