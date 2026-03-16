package com.example.aviationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "airport", indexes = @Index(name = "idx_airport_code", unique = true, columnList = "code"))
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    @NotNull
    private String code;

    @Column(nullable = false, length = 200)
    @NotNull
    private String name;

    @Column(nullable = false, length = 100)
    @NotNull
    private String city;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
