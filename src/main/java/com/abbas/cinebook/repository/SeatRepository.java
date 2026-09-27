package com.abbas.cinebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abbas.cinebook.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByScreenId(int screenId);
}
