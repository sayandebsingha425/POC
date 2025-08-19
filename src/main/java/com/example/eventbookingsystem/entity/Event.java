package com.example.eventbookingsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int totalSeats;
    private int bookedSeats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public int getBookedSeats() {
		return bookedSeats;
	}
	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Event(Long id, String name, int totalSeats, int bookedSeats, LocalDateTime startTime,
			LocalDateTime endTime) {
		super();
		this.id = id;
		this.name = name;
		this.totalSeats = totalSeats;
		this.bookedSeats = bookedSeats;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
}