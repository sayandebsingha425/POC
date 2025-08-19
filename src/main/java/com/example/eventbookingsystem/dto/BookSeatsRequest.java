package com.example.eventbookingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookSeatsRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Min(value = 1, message = "Must book at least 1 seat")
    private int seats;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
}