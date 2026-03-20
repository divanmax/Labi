package com.example.aviationservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    public static final String CLAIM_TYPE = "type";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_REFRESH_JTI = "rjti";
    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    @Value("${security.jwt.secret:change-me-to-at-least-32-chars-secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.access-expiration-seconds:900}")
    private long accessExpirationSec;

    @Value("${security.jwt.refresh-expiration-seconds:604800}")
    private long refreshExpirationSec;

    private SecretKey key;

    @PostConstruct
    public void init() {
        if (jwtSecret.length() < 32) {
            throw new IllegalStateException("security.jwt.secret must be at least 32 chars");
        }
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpirationSec);
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(Map.of(CLAIM_TYPE, TYPE_ACCESS, CLAIM_ROLE, role))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String role, String refreshTokenId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshExpirationSec);
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(Map.of(
                        CLAIM_TYPE, TYPE_REFRESH,
                        CLAIM_ROLE, role,
                        CLAIM_REFRESH_JTI, refreshTokenId
                ))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(parse(token).getPayload().get(CLAIM_TYPE, String.class));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(parse(token).getPayload().get(CLAIM_TYPE, String.class));
    }

    public String getUsername(String token) {
        return parse(token).getPayload().getSubject();
    }

    public String getRole(String token) {
        return parse(token).getPayload().get(CLAIM_ROLE, String.class);
    }

    public String getRefreshTokenId(String token) {
        return parse(token).getPayload().get(CLAIM_REFRESH_JTI, String.class);
    }

    public long getAccessExpirationSec() {
        return accessExpirationSec;
    }

    public long getRefreshExpirationSec() {
        return refreshExpirationSec;
    }

    public String newRefreshTokenId() {
        return UUID.randomUUID().toString();
    }
}

