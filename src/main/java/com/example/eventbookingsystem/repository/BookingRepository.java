package com.example.eventbookingsystem.repository;

import com.example.eventbookingsystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Custom query to find all bookings made by a specific user.
     */
    List<Booking> findByUserId(Long userId);
}