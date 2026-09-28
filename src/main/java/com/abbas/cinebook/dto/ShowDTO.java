package com.abbas.cinebook.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ShowDTO {

    private LocalDateTime showTime;
    private int movieId;
    private int screenId;

}