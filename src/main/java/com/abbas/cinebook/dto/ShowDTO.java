package com.abbas.cinebook.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ShowDTO {

    @NotNull(message = "Show time is required")
    @Future(message = "Show time must be in future")
    private LocalDateTime showTime;

    @NotNull(message = "Movie id is required")
    @Positive(message = "Movie id is required")
    private Integer movieId;

    @NotNull(message = "Screen id is required")
    @Positive(message = "Screen id is required")
    private Integer screenId;

}