package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
    
}
