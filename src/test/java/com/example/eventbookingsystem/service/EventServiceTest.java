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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private EventService eventService;

    private Event mockEvent;
    private Booking mockBooking;

    @BeforeEach
    void setUp() {
        // Reusable mock objects for tests
        mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setName("Tech Conference");
        mockEvent.setTotalSeats(100);
        mockEvent.setBookedSeats(50);

        mockBooking = new Booking();
        mockBooking.setId(99L);
        mockBooking.setEventId(1L);
        mockBooking.setUserId(101L);
        mockBooking.setSeatsBooked(2);
    }

    @Test
    void createEvent_shouldSaveAndReturnEvent() {
        // --- ARRANGE ---
        CreateEventRequest request = new CreateEventRequest();
        request.setName("New Event");
        request.setTotalSeats(150);
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        // When eventRepository.save is called, return the saved entity
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // --- ACT ---
        Event createdEvent = eventService.createEvent(request);

        // --- ASSERT ---
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.getName()).isEqualTo("New Event");
        assertThat(createdEvent.getTotalSeats()).isEqualTo(150);
        assertThat(createdEvent.getBookedSeats()).isZero(); // Should be initialized to 0
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void listAvailableEvents_shouldReturnOnlyEventsWithSeats() {
        // --- ARRANGE ---
        Event fullEvent = new Event();
        fullEvent.setTotalSeats(50);
        fullEvent.setBookedSeats(50);

        when(eventRepository.findAll()).thenReturn(List.of(mockEvent, fullEvent));

        // --- ACT ---
        List<Event> availableEvents = eventService.listAvailableEvents();

        // --- ASSERT ---
        assertThat(availableEvents).hasSize(1);
        assertThat(availableEvents.get(0).getName()).isEqualTo("Tech Conference");
    }
    
    @Test
    void bookSeats_shouldSucceed_whenSeatsAreAvailable() {
        // --- ARRANGE ---
        BookSeatsRequest request = new BookSeatsRequest();
        request.setUserId(101L);
        request.setSeats(5);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        // --- ACT ---
        BookingResponse response = eventService.bookSeats(1L, request);

        // --- ASSERT ---
        assertThat(response.getBookingId()).isEqualTo(99L);
        assertThat(mockEvent.getBookedSeats()).isEqualTo(55); // 50 + 5
        assertThat(response.getRemainingSeats()).isEqualTo(45); // 100 - 55
        verify(eventRepository, times(1)).save(mockEvent);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void bookSeats_shouldThrowNotEnoughSeatsException_whenSeatsAreUnavailable() {
        // --- ARRANGE ---
        mockEvent.setBookedSeats(98); // Only 2 seats left
        BookSeatsRequest request = new BookSeatsRequest();
        request.setUserId(101L);
        request.setSeats(3); // Requesting 3

        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));

        // --- ACT & ASSERT ---
        assertThrows(NotEnoughSeatsException.class, () -> eventService.bookSeats(1L, request));
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void bookSeats_shouldThrowEventNotFoundException_whenEventDoesNotExist() {
        // --- ARRANGE ---
        when(eventRepository.findById(404L)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(EventNotFoundException.class, () -> eventService.bookSeats(404L, new BookSeatsRequest()));
    }
    
    @Test
    void getUserBookings_shouldReturnBookingsForUser() {
        // --- ARRANGE ---
        when(bookingRepository.findByUserId(101L)).thenReturn(List.of(mockBooking));

        // --- ACT ---
        List<Booking> bookings = eventService.getUserBookings(101L);

        // --- ASSERT ---
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getId()).isEqualTo(99L);
    }
    
    @Test
    void getUserBookings_shouldReturnEmptyList_whenUserHasNoBookings() {
        // --- ARRANGE ---
        when(bookingRepository.findByUserId(102L)).thenReturn(Collections.emptyList());

        // --- ACT ---
        List<Booking> bookings = eventService.getUserBookings(102L);

        // --- ASSERT ---
        assertThat(bookings).isNotNull().isEmpty();
    }
    
    @Test
    void cancelBooking_shouldSucceedAndFreeUpSeats() {
        // --- ARRANGE ---
        when(bookingRepository.findById(99L)).thenReturn(Optional.of(mockBooking));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));

        // --- ACT ---
        eventService.cancelBooking(99L);

        // --- ASSERT ---
        assertThat(mockEvent.getBookedSeats()).isEqualTo(48); // 50 - 2
        verify(eventRepository, times(1)).save(mockEvent);
        verify(bookingRepository, times(1)).delete(mockBooking);
    }

    @Test
    void cancelBooking_shouldThrowException_whenBookingNotFound() {
        // --- ARRANGE ---
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(RuntimeException.class, () -> eventService.cancelBooking(999L));
    }

    @Test
    void cancelBooking_shouldThrowException_whenEventForBookingNotFound() {
        // --- ARRANGE ---
        when(bookingRepository.findById(99L)).thenReturn(Optional.of(mockBooking));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(EventNotFoundException.class, () -> eventService.cancelBooking(99L));
    }
}