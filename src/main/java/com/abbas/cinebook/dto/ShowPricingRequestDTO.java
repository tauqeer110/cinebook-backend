package com.abbas.cinebook.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ShowPricingRequestDTO {

    @NotEmpty(message = "At least one price must be provided")
    @Valid // Ensure each SeatPriceDTO inside list must follow SeatPriceDTO validation rules
    private List<SeatPriceDTO> prices;
}
