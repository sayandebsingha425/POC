package com.example.eventbookingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventNotFoundException(EventNotFoundException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughSeatsException.class)
    public ResponseEntity<Map<String, String>> handleNotEnoughSeatsException(NotEnoughSeatsException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }
}