package com.abbas.cinebook.dto;

import com.abbas.cinebook.enums.SeatType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SeatPriceDTO {

    @NotNull(message = "Seat Type is required")
    private SeatType seatType;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
}
