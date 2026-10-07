package com.abbas.cinebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {
    List<BookingSeat> findByBookingId(int bookingId);
} 


