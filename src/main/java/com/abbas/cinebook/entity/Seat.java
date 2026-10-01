package com.abbas.cinebook.entity;

import com.abbas.cinebook.enums.SeatType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class Seat {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @ManyToOne 
    @JoinColumn(name = "screen_id")
    private Screen screen;

}
