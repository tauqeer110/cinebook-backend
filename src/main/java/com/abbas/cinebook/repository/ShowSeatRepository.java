package com.abbas.cinebook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abbas.cinebook.entity.ShowSeat;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Integer> {
    Optional<ShowSeat> findByShowIdAndSeatId(Integer showId, Integer seatId);
}
