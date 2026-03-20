package com.example.aviationservice.controller;

import com.example.aviationservice.dto.RegisterRequest;
import com.example.aviationservice.entity.AppUserEntity;
import com.example.aviationservice.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Pattern SPECIAL_SYMBOLS = Pattern.compile("[^A-Za-z0-9]");

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        if (!isPasswordStrong(request.getPassword())) {
            return ResponseEntity.badRequest().body("Password must be length >= 8 and contain special symbol");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        AppUserEntity user = new AppUserEntity();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("username", user.getUsername(), "role", user.getRole()));
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("csrfToken", token.getToken());
    }

    private static boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        return SPECIAL_SYMBOLS.matcher(password).find();
    }
}

