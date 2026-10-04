package com.abbas.cinebook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MovieRequestDTO {

    @NotBlank(message = "Movie title is required")
    private String title;

    @NotNull(message = "Duration is required")
    @Min(value = 60, message = "Duration must be at least 60 minutes")
    private Double duration;
}
