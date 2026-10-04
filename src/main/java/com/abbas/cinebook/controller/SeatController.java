package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.SeatResponseDTO;
import com.abbas.cinebook.service.SeatService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController 
@Validated 
@AllArgsConstructor
@RequestMapping("/api") 
public class SeatController {
    
    private final SeatService seatService;

    @GetMapping("/screens/{screenId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats(

        @Positive(message = "Screen id must be positive")
        @PathVariable int screenId) {
        
        List<SeatResponseDTO> response = 
                seatService.getAllSeats(screenId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
