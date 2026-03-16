package com.example.aviationservice.dto;

import java.util.List;

public class AvailableSeatsResponse {
    private Long flightId;
    private int capacity;
    private long bookedCount;
    private int availableCount;
    private List<String> bookedSeatNumbers;

    public AvailableSeatsResponse() {}
    public AvailableSeatsResponse(Long flightId, int capacity, long bookedCount, List<String> bookedSeatNumbers) {
        this.flightId = flightId;
        this.capacity = capacity;
        this.bookedCount = bookedCount;
        this.availableCount = Math.max(0, capacity - (int) bookedCount);
        this.bookedSeatNumbers = bookedSeatNumbers != null ? bookedSeatNumbers : List.of();
    }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public long getBookedCount() { return bookedCount; }
    public void setBookedCount(long bookedCount) { this.bookedCount = bookedCount; }
    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }
    public List<String> getBookedSeatNumbers() { return bookedSeatNumbers; }
    public void setBookedSeatNumbers(List<String> bookedSeatNumbers) { this.bookedSeatNumbers = bookedSeatNumbers; }
}
