package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.ScreenRequestDTO;
import com.abbas.cinebook.dto.ScreenResponseDTO;
import com.abbas.cinebook.service.ScreenService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@AllArgsConstructor 
@RequestMapping("/api/screens")
public class ScreenController {
    
    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenResponseDTO> getMethodName(@RequestBody ScreenRequestDTO screenRequestDTO) {

        ScreenResponseDTO response = 
                screenService.createScreen(screenRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
}
