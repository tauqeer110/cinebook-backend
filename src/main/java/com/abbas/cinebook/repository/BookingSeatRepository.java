package com.abbas.cinebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.BookingSeat;
import com.abbas.cinebook.enums.BookingStatus;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {
    boolean existsBySeatIdAndBooking_ShowIdAndBooking_BookingStatus(
    int seatId, int showId, BookingStatus status);
    List<BookingSeat> findByBookingId(int bookingId);
} 
