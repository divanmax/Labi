package com.example.aviationservice.security;

import com.example.aviationservice.entity.AppUserEntity;
import com.example.aviationservice.repository.AppUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserSeeder {

    @Value("${APP_ADMIN_USERNAME:}")
    private String adminUsername;

    @Value("${APP_ADMIN_PASSWORD:}")
    private String adminPassword;

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserSeeder(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            return;
        }
        if (adminUsername == null || adminUsername.isBlank()) {
            return;
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        AppUserEntity user = new AppUserEntity();
        user.setUsername(adminUsername);
        user.setPasswordHash(passwordEncoder.encode(adminPassword));
        user.setRole("ADMIN");
        user.setEnabled(true);
        userRepository.save(user);
    }
}

