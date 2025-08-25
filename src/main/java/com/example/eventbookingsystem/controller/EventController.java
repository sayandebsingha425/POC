package com.example.eventbookingsystem.controller;

import com.example.eventbookingsystem.dto.CreateEventRequest;
import com.example.eventbookingsystem.entity.Event;
import com.example.eventbookingsystem.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    
    @PostMapping("/internal/{eventId}/update-seats")
    public ResponseEntity<Void> updateBookedSeats(@PathVariable Long eventId, @RequestBody Map<String, Integer> payload) {
        int seats = payload.get("seats");
        eventService.updateBookedSeats(eventId, seats);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

}