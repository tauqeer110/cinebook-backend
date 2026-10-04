package com.abbas.cinebook.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ScreenRequestDTO {

    @NotNull(message = "Screen number is required")
    @Positive(message = "Screen number must be greater than 0")
    private Integer screenNumber;

    @NotNull(message = "total seats are required")
    @Positive(message = "Total seats must be greater than 0")
    private Integer totalSeats;

    @NotNull(message = "Theater id is required")
    @Positive(message = "Theater id must be valid")
    private Integer theaterId;
}
