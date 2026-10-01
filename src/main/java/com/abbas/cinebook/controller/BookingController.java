package com.abbas.cinebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abbas.cinebook.dto.BookingRequestDTO;
import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.service.BookingService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO dto) {

        BookingResponseDTO response =
                bookingService.createBooking(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable int bookingId) {
        
        BookingResponseDTO response = 
                bookingService.getBooking(bookingId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable int bookingId) {
        
        BookingResponseDTO response = 
                bookingService.cancelBooking(bookingId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}