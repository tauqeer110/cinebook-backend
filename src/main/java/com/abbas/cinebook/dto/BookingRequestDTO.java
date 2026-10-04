package com.abbas.cinebook.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class BookingRequestDTO {

    @NotNull(message = "Show id is required")
    @Positive(message = "Show id must be Positive")
    private Integer showId;

    @NotNull(message = "Show id is required")
    @Positive(message = "User id must be valid")
    private Integer userId;

    @NotEmpty(message = "At least one seat id is required")
    private List<@Positive(message = "Seat id must be valid") Integer> seatIds;
}
