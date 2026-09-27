package com.abbas.cinebook.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.SeatResponseDTO;
import com.abbas.cinebook.entity.Seat;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.ScreenRepository;
import com.abbas.cinebook.repository.SeatRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    
    public List<SeatResponseDTO> getAllSeats(int screenId) {

        screenRepository.findById(screenId)
            .orElseThrow(() -> new ResourceNotFoundException("Screen Id Does Not Exist"));

        List<Seat> seats = seatRepository.findByScreenId(screenId);            


        return seats.stream()
            .map(seat -> {
                SeatResponseDTO dto = new SeatResponseDTO();
                dto.setId(seat.getId());
                dto.setSeatNumber(seat.getSeatNumber());
                dto.setSeatType(seat.getSeatType());
                return dto;
            })
            .toList();
    }
}
