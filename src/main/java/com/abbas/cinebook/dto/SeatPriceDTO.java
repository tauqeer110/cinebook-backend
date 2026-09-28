package com.abbas.cinebook.dto;

import com.abbas.cinebook.enums.SeatType;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SeatPriceDTO {
    private SeatType seatType;
    private double price;
}
