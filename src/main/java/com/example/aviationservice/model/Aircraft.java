package com.example.aviationservice.model;

public class Aircraft {
    private Long id;
    private String model;
    private String registrationCode;
    private int capacity;

    public Aircraft() {
    }

    public Aircraft(Long id, String model, String registrationCode, int capacity) {
        this.id = id;
        this.model = model;
        this.registrationCode = registrationCode;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
