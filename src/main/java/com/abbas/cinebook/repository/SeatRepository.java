package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abbas.cinebook.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    
}
