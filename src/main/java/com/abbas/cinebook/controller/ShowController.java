package com.abbas.cinebook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abbas.cinebook.dto.ShowDTO;
import com.abbas.cinebook.dto.ShowPricingRequestDTO;
import com.abbas.cinebook.dto.ShowPricingResponseDTO;
import com.abbas.cinebook.dto.ShowResponseDTO;
import com.abbas.cinebook.service.ShowPricingService;
import com.abbas.cinebook.service.ShowService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController 
@Validated 
@RequestMapping("/api/shows")
@RequiredArgsConstructor 
public class ShowController {

    private final ShowService showService;
    private final ShowPricingService showPricingService;
    
    @PostMapping
    public ResponseEntity<ShowResponseDTO> createShow(
        
        @Valid 
        @RequestBody ShowDTO dto) {

        ShowResponseDTO response = 
                showService.createShow(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{showId}/pricing")
    public ResponseEntity<List<ShowPricingResponseDTO>> setShowPrice(

        @Positive(message = "Show id must be positive")
        @PathVariable int showId, 

        @Valid 
        @RequestBody ShowPricingRequestDTO showPricingRequestDTO) {
      
        List<ShowPricingResponseDTO> response = 
                showPricingService.setShowPrice(showId, showPricingRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ShowResponseDTO>> getShows() {

        List<ShowResponseDTO> showResponseDTOs = 
                showService.getShows();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(showResponseDTOs);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ShowResponseDTO> getShow(

        @Positive(message = "Show id must be positive")
        @PathVariable  int showId) {

        ShowResponseDTO showResponseDTO = 
                showService.getShow(showId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(showResponseDTO);
    }
    
}
