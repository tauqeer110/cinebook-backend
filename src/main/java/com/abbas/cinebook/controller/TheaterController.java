package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.TheaterRequestDTO;
import com.abbas.cinebook.dto.TheaterResponseDTO;
import com.abbas.cinebook.service.TheaterService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@AllArgsConstructor 
@RequestMapping("/api/theaters")
public class TheaterController {
    
    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterResponseDTO> createTheater(@RequestBody TheaterRequestDTO theaterRequestDTO) {
        
        TheaterResponseDTO response = 
                theaterService.createTheater(theaterRequestDTO);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
}
