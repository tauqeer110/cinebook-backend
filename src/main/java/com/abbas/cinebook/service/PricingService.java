package com.abbas.cinebook.service;

import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import com.abbas.cinebook.entity.Seat;
import com.abbas.cinebook.entity.Show;
import com.abbas.cinebook.entity.ShowPricing;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.ShowPricingRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class PricingService {

    private final ShowPricingRepository showPricingRepository;

    public double calculatePrice(Seat seat, Show show) {

        ShowPricing showPricing = showPricingRepository
            .findByShowIdAndSeatType(show.getId(), seat.getSeatType())
            .orElseThrow(() -> new ResourceNotFoundException("Price not set for this show and seat type"));

        return showPricing.getPrice();
    }
}
