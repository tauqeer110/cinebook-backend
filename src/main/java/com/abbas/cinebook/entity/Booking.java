package com.abbas.cinebook.entity;

import java.time.LocalDateTime;

import com.abbas.cinebook.enums.BookingStatus;

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
public class Booking {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;
    private LocalDateTime bookingTime;


    @ManyToOne 
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne 
    @JoinColumn(name = "show_id")
    private Show show;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

}
