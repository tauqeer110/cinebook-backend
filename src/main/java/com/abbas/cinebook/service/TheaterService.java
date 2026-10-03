package com.abbas.cinebook.service;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.TheaterRequestDTO;
import com.abbas.cinebook.dto.TheaterResponseDTO;
import com.abbas.cinebook.entity.Theater;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.repository.TheaterRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class TheaterService {
    
    private final TheaterRepository theaterRepository;

    public TheaterResponseDTO createTheater(TheaterRequestDTO theaterRequestDTO) {

        boolean isExists = theaterRepository.existsByNameAndLocation(theaterRequestDTO.getName(), theaterRequestDTO.getLocation());

        if(isExists) throw new ConflictException("Theater Already Exists With The Same Location");

        Theater theater = new Theater();

        theater.setName(theaterRequestDTO.getName());
        theater.setLocation(theaterRequestDTO.getLocation());

        Theater savedTheater =  theaterRepository.save(theater);

        TheaterResponseDTO theaterResponseDTO = new TheaterResponseDTO();

        theaterResponseDTO.setId(savedTheater.getId());
        theaterResponseDTO.setName(savedTheater.getName());
        theaterResponseDTO.setLocation(savedTheater.getLocation());

        return theaterResponseDTO;

    }
}
