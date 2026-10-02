package com.abbas.cinebook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abbas.cinebook.entity.ShowPricing;
import com.abbas.cinebook.enums.SeatType;

public interface ShowPricingRepository extends JpaRepository<ShowPricing, Integer> {
    Optional<ShowPricing> findByShowIdAndSeatType(int showId, SeatType seatType);
    List<ShowPricing> findByShowId(int showId);
}
