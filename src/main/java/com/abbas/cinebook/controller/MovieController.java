package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.MovieRequestDTO;
import com.abbas.cinebook.dto.MovieResponseDTO;
import com.abbas.cinebook.service.MovieService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@AllArgsConstructor 
@RequestMapping("/api/movies")
public class MovieController {
    
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponseDTO> createMovie(
        
        @Valid 
        @RequestBody MovieRequestDTO movieRequestDTO) {
        
        MovieResponseDTO response = 
                movieService.createMovie(movieRequestDTO);
        
        return ResponseEntity 
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
}
