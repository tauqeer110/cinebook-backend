package com.abbas.cinebook.entity;

import jakarta.persistence.Entity;
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
public class BookingSeat {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double price;

    @ManyToOne 
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne 
    @JoinColumn(name = "seat_id")
    private Seat seat;

}
