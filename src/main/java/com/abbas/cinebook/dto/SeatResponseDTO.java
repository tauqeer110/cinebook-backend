package com.abbas.cinebook.dto;

import com.abbas.cinebook.enums.SeatType;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SeatResponseDTO {
    private int id;
    private String SeatNumber;
    private SeatType seatType;
}
