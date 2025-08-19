package com.example.eventbookingsystem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateEventRequest {
    @NotBlank(message = "Event name cannot be blank")
    private String name;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}