package com.abbas.cinebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
    List<Booking> findByUserId(int userId);
}
