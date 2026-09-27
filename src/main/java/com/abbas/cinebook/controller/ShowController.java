package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.ShowDTO;
import com.abbas.cinebook.dto.ShowResponseDTO;
import com.abbas.cinebook.service.ShowService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/shows")
@RequiredArgsConstructor 
public class ShowController {

    private final ShowService showService;
    
    @PostMapping
    public ResponseEntity<ShowResponseDTO> createShow(@RequestBody ShowDTO dto) {
        ShowResponseDTO response = showService.createShow(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
