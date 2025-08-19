package com.example.eventbookingsystem.repository;

import com.example.eventbookingsystem.entity.Event;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Finds an event by ID and applies a pessimistic write lock.
     * This prevents other transactions from modifying the event while this transaction is in progress.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<Event> findById(Long id);
}