package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.dto.UserRequestDTO;
import com.abbas.cinebook.dto.UserResponseDTO;
import com.abbas.cinebook.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@Validated 
@AllArgsConstructor 
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
        
        @Valid 
        @RequestBody UserRequestDTO userRequestDTO) {
        
        UserResponseDTO response = 
                userService.createUser(userRequestDTO);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMethodName(
        
        @Positive(message = "User id must be positive")
        @PathVariable int userId) {
        
        List<BookingResponseDTO> bookingResponseDTOs = 
                userService.getBookings(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingResponseDTOs);
    }

}
