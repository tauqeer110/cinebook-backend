package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.service.UserService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController 
@AllArgsConstructor 
@RequestMapping("/api/users")
public class UserController {
    
    private UserService userService;

    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMethodName(@PathVariable int userId) {
        
        List<BookingResponseDTO> bookingResponseDTOs = 
                userService.getBookings(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingResponseDTOs);
    }
    
}
