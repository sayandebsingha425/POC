package com.example.eventbookingsystem.service;

import com.example.eventbookingsystem.dto.BookSeatsRequest;
import com.example.eventbookingsystem.dto.BookingResponse;
import com.example.eventbookingsystem.dto.CreateEventRequest;
import com.example.eventbookingsystem.entity.Booking;
import com.example.eventbookingsystem.entity.Event;
import com.example.eventbookingsystem.exception.EventNotFoundException;
import com.example.eventbookingsystem.exception.NotEnoughSeatsException;
import com.example.eventbookingsystem.repository.BookingRepository;
import com.example.eventbookingsystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public EventService(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
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

    @Transactional
    public BookingResponse bookSeats(Long eventId, BookSeatsRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + eventId + " not found."));

        int availableSeats = event.getTotalSeats() - event.getBookedSeats();
        if (availableSeats < request.getSeats()) {
            throw new NotEnoughSeatsException("Not enough seats available. Requested: " + request.getSeats() + ", Available: " + availableSeats);
        }

        event.setBookedSeats(event.getBookedSeats() + request.getSeats());
        eventRepository.save(event);

        Booking booking = new Booking();
        booking.setEventId(eventId);
        booking.setUserId(request.getUserId());
        booking.setSeatsBooked(request.getSeats());
        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(savedBooking.getId(), event.getName(), event.getTotalSeats() - event.getBookedSeats());
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking with ID " + bookingId + " not found"));

        Event event = eventRepository.findById(booking.getEventId())
            .orElseThrow(() -> new EventNotFoundException("Associated event with ID " + booking.getEventId() + " not found for the booking."));

        event.setBookedSeats(event.getBookedSeats() - booking.getSeatsBooked());
        eventRepository.save(event);

        bookingRepository.delete(booking);
    }
}