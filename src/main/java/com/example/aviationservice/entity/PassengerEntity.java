package com.example.aviationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "passenger", indexes = @Index(name = "idx_passenger_passport", unique = true, columnList = "passport_number"))
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    @NotNull
    private String lastName;

    @Column(nullable = false, length = 255)
    @NotNull
    private String email;

    @Column(name = "passport_number", nullable = false, unique = true, length = 50)
    @NotNull
    private String passportNumber;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
}
