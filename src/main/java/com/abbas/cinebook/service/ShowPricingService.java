package com.abbas.cinebook.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.ShowPricingRequestDTO;
import com.abbas.cinebook.dto.ShowPricingResponseDTO;
import com.abbas.cinebook.entity.Show;
import com.abbas.cinebook.entity.ShowPricing;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.ShowPricingRepository;
import com.abbas.cinebook.repository.ShowRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class ShowPricingService {

    private final ShowRepository showRepository;
    private final ShowPricingRepository showPricingRepository;
    
    public List<ShowPricingResponseDTO> setShowPrice(
        
        int showId,
        
        ShowPricingRequestDTO showPricingRequestDto) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Show Does Not Exist"));

        return showPricingRequestDto.getPrices()
            .stream()
            .map(seatPriceDto -> {

                ShowPricing showPricing = showPricingRepository
                        .findByShowIdAndSeatType(
                                showId,
                                seatPriceDto.getSeatType()
                        )
                        .orElseGet(() -> {
                            ShowPricing newPricing = new ShowPricing();
                            newPricing.setShow(show);
                            newPricing.setSeatType(seatPriceDto.getSeatType());
                            return newPricing;
                        });

                showPricing.setPrice(seatPriceDto.getPrice());

                ShowPricing saved =
                        showPricingRepository.save(showPricing);

                ShowPricingResponseDTO response =
                        new ShowPricingResponseDTO();

                response.setPrice(saved.getPrice());
                response.setSeatType(saved.getSeatType());
                response.setShowId(showId);

                return response;
            })
            .toList();
    }
}
