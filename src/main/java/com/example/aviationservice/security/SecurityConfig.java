package com.example.aviationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/register"))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/csrf").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/api/aircraft", "/api/airports", "/api/flights", "/api/passengers")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/aircraft/**", "/api/airports/**", "/api/flights/**", "/api/passengers/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/aircraft/**", "/api/airports/**", "/api/flights/**", "/api/passengers/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/operations/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

