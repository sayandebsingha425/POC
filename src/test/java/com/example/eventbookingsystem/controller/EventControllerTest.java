package com.example.eventbookingsystem.controller;

import com.example.eventbookingsystem.dto.BookSeatsRequest;
import com.example.eventbookingsystem.dto.BookingResponse;
import com.example.eventbookingsystem.dto.CreateEventRequest;
import com.example.eventbookingsystem.entity.Booking;
import com.example.eventbookingsystem.entity.Event;
import com.example.eventbookingsystem.exception.EventNotFoundException;
import com.example.eventbookingsystem.exception.NotEnoughSeatsException;
import com.example.eventbookingsystem.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEvent_shouldReturn201Created_whenRequestIsValid() throws Exception {
        CreateEventRequest request = new CreateEventRequest();
        request.setName("New Year Party");
        request.setTotalSeats(200);
        request.setStartTime(LocalDateTime.now().plusMonths(1));
        request.setEndTime(LocalDateTime.now().plusMonths(1).plusHours(4));

        when(eventService.createEvent(any(CreateEventRequest.class))).thenReturn(new Event());

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEvent_shouldReturn400BadRequest_whenNameIsBlank() throws Exception {
        CreateEventRequest request = new CreateEventRequest();
        request.setName(""); // Invalid
        request.setTotalSeats(50);
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(2));

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listEvents_shouldReturn200OkWithListOfEvents() throws Exception {
        when(eventService.listAvailableEvents()).thenReturn(List.of(new Event()));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void bookSeats_shouldReturn200Ok_whenSuccessful() throws Exception {
        BookSeatsRequest request = new BookSeatsRequest();
        request.setUserId(101L);
        request.setSeats(2);
        BookingResponse response = new BookingResponse(99L, "Test Event", 48);

        when(eventService.bookSeats(eq(1L), any(BookSeatsRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/events/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(99L));
    }
    
    @Test
    void bookSeats_shouldReturn409Conflict_whenNotEnoughSeats() throws Exception {
        BookSeatsRequest request = new BookSeatsRequest();
        request.setUserId(101L);
        request.setSeats(5);
        
        when(eventService.bookSeats(eq(1L), any(BookSeatsRequest.class)))
            .thenThrow(new NotEnoughSeatsException("Not enough seats."));
        
        mockMvc.perform(post("/api/events/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Not enough seats."));
    }

    @Test
    void bookSeats_shouldReturn404NotFound_whenEventDoesNotExist() throws Exception {
        BookSeatsRequest request = new BookSeatsRequest();
        request.setUserId(101L);
        request.setSeats(1);

        when(eventService.bookSeats(eq(404L), any(BookSeatsRequest.class)))
            .thenThrow(new EventNotFoundException("Event not found."));

        mockMvc.perform(post("/api/events/404/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Event not found."));
    }

    @Test
    void getUserBookings_shouldReturn200OkWithBookings() throws Exception {
        when(eventService.getUserBookings(101L)).thenReturn(List.of(new Booking()));

        mockMvc.perform(get("/api/users/101/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void getUserBookings_shouldReturn200OkWithEmptyList_whenNoBookings() throws Exception {
        when(eventService.getUserBookings(102L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/102/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void cancelBooking_shouldReturn204NoContent_whenSuccessful() throws Exception {
        doNothing().when(eventService).cancelBooking(99L); // For void methods

        mockMvc.perform(delete("/api/bookings/99"))
                .andExpect(status().isNoContent());
    }
}