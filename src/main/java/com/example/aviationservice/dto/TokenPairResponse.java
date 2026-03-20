package com.example.aviationservice.dto;

public class TokenPairResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long accessExpiresAtEpochSec;
    private long refreshExpiresAtEpochSec;

    public TokenPairResponse() {
    }

    public TokenPairResponse(String tokenType, String accessToken, String refreshToken,
                             long accessExpiresAtEpochSec, long refreshExpiresAtEpochSec) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpiresAtEpochSec = accessExpiresAtEpochSec;
        this.refreshExpiresAtEpochSec = refreshExpiresAtEpochSec;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getAccessExpiresAtEpochSec() {
        return accessExpiresAtEpochSec;
    }

    public void setAccessExpiresAtEpochSec(long accessExpiresAtEpochSec) {
        this.accessExpiresAtEpochSec = accessExpiresAtEpochSec;
    }

    public long getRefreshExpiresAtEpochSec() {
        return refreshExpiresAtEpochSec;
    }

    public void setRefreshExpiresAtEpochSec(long refreshExpiresAtEpochSec) {
        this.refreshExpiresAtEpochSec = refreshExpiresAtEpochSec;
    }
}

