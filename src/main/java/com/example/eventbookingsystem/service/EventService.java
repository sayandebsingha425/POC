package com.example.eventbookingsystem.service;


import com.example.eventbookingsystem.dto.CreateEventRequest;
import com.example.eventbookingsystem.entity.Event;
import com.example.eventbookingsystem.exception.EventNotFoundException;
import com.example.eventbookingsystem.exception.NotEnoughSeatsException;
import com.example.eventbookingsystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
 

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        
    }

    @Transactional
    public Event createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setTotalSeats(request.getTotalSeats());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setBookedSeats(0);
        return eventRepository.save(event);
    }

    public List<Event> listAvailableEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getBookedSeats() < event.getTotalSeats())
                .collect(Collectors.toList());
    }


 // In event-service's EventService.java

    @Transactional
    public void updateBookedSeats(Long eventId, int seatsToBook) {
        // 1. Find the event and lock the database row
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        // 2. Perform the availability check within the locked transaction
        if (seatsToBook > 0 && (event.getTotalSeats() - event.getBookedSeats()) < seatsToBook) {
            // This is the crucial check to prevent overbooking
            throw new NotEnoughSeatsException("Not enough seats available"); // We need to create this exception
        }

        // 3. If the check passes, update the seat count
        event.setBookedSeats(event.getBookedSeats() + seatsToBook);
        eventRepository.save(event);
    }
    
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }
}