package com.example.eventbookingsystem.entity;

import jakarta.persistence.*;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private Long userId;
    private int seatsBooked;
    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getSeatsBooked() {
		return seatsBooked;
	}
	public void setSeatsBooked(int seatsBooked) {
		this.seatsBooked = seatsBooked;
	}
	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Booking(Long id, Long eventId, Long userId, int seatsBooked) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.userId = userId;
		this.seatsBooked = seatsBooked;
	}

   
}