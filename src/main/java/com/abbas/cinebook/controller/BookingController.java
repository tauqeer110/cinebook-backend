package com.abbas.cinebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abbas.cinebook.dto.BookingRequestDTO;
import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.exception.BadRequestException;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.service.BookingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO dto)
            throws BadRequestException, ConflictException {

        BookingResponseDTO response =
                bookingService.createBooking(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}