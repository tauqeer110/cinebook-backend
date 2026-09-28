package com.abbas.cinebook.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ShowPricingRequestDTO {
    private List<SeatPriceDTO> prices;
}
