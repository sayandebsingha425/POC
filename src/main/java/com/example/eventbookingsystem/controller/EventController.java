package com.example.eventbookingsystem.controller;

import com.example.eventbookingsystem.dto.BookSeatsRequest;
import com.example.eventbookingsystem.dto.BookingResponse;
import com.example.eventbookingsystem.dto.CreateEventRequest;
import com.example.eventbookingsystem.entity.Booking;
import com.example.eventbookingsystem.entity.Event;
import com.example.eventbookingsystem.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event createdEvent = eventService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> availableEvents = eventService.listAvailableEvents();
        return ResponseEntity.ok(availableEvents);
    }

    @PostMapping("/events/{eventId}/book")
    public ResponseEntity<BookingResponse> bookSeats(@PathVariable Long eventId, @Valid @RequestBody BookSeatsRequest request) {
        BookingResponse bookingResponse = eventService.bookSeats(eventId, request);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/users/{userId}/bookings")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> userBookings = eventService.getUserBookings(userId);
        return ResponseEntity.ok(userBookings);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        eventService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}