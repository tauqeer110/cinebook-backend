package com.abbas.cinebook.service;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.ScreenRequestDTO;
import com.abbas.cinebook.dto.ScreenResponseDTO;
import com.abbas.cinebook.entity.Screen;
import com.abbas.cinebook.entity.Theater;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.ScreenRepository;
import com.abbas.cinebook.repository.TheaterRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class ScreenService {
    
    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;

    public ScreenResponseDTO createScreen(ScreenRequestDTO screenRequestDTO) {

        Theater theater =  theaterRepository.findById(screenRequestDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater Does Not Exists"));

        boolean isScreenExists = screenRepository.existsByTheaterIdAndScreenNumber(screenRequestDTO.getTheaterId(), screenRequestDTO.getScreenNumber());

        if(isScreenExists) throw new ConflictException("Screen Number Is Already Exists In This Theater");

        Screen screen = new Screen();

        screen.setScreenNumber(screenRequestDTO.getScreenNumber());
        screen.setTotalSeats(screenRequestDTO.getTotalSeats());
        screen.setTheater(theater);

        Screen savedScreen = screenRepository.save(screen);

        ScreenResponseDTO screenResponseDTO = new ScreenResponseDTO();

        screenResponseDTO.setScreenId(savedScreen.getId());
        screenResponseDTO.setScreenNumber(savedScreen.getScreenNumber());
        screenResponseDTO.setTheaterId(savedScreen.getTheater().getId());
        screenResponseDTO.setTotalSeats(savedScreen.getTotalSeats());

        return screenResponseDTO;
    }
}
