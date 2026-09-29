package com.abbas.cinebook.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class BookingRequestDTO {
    private int showId;
    private int userId;
    private List<Integer> seatIds;
}
