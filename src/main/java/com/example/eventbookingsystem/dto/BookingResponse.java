package com.example.eventbookingsystem.dto;

public class BookingResponse {
    private Long bookingId;
    private String eventName;
    private int remainingSeats;

    public BookingResponse(Long bookingId, String eventName, int remainingSeats) {
        this.bookingId = bookingId;
        this.eventName = eventName;
        this.remainingSeats = remainingSeats;
    }

    // Getters and Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public int getRemainingSeats() { return remainingSeats; }
    public void setRemainingSeats(int remainingSeats) { this.remainingSeats = remainingSeats; }
}