package com.abbas.cinebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.abbas.cinebook.entity.ShowSeat;
import com.abbas.cinebook.enums.SeatStatus;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Integer> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE ShowSeat ss
        SET ss.seatStatus = :targetStatus
        WHERE ss.show.id = :showId
        AND ss.seat.id IN :seatIds
        AND ss.seatStatus = :currentStatus
    """)
    int updateSeatStatusConditionally(
            int showId,
            List<Integer> seatIds,
            SeatStatus currentStatus,
            SeatStatus targetStatus
    );
}
