package com.abbas.cinebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.BookingRequestDTO;
import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.service.BookingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@Validated 
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            
        @Valid  
        @RequestBody BookingRequestDTO dto) {

        BookingResponseDTO response =
                bookingService.createBooking(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBooking(
        
        @Positive(message = "Booking id must be positive")
        @PathVariable int bookingId) {
        
        BookingResponseDTO response = 
                bookingService.getBooking(bookingId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
        
        @Positive(message = "Booking id must be positive")
        @PathVariable int bookingId) {
        
        BookingResponseDTO response = 
                bookingService.cancelBooking(bookingId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}