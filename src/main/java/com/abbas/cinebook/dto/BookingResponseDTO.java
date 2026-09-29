package com.abbas.cinebook.dto;

import java.util.List;

import com.abbas.cinebook.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
public class BookingResponseDTO {
    private int bookingId;
    private BookingStatus bookingStatus;
    private int showId;
    private List<String> seatNumbers;
    private double totalPrice;
}
