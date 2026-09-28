package com.abbas.cinebook.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ShowResponseDTO {
    private int id;
    private LocalDateTime showTime;
    private String movieTitle;
    private int screenNumber;
}
