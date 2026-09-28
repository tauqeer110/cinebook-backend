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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"show_id", "seat_type"}))
public class ShowPricing {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne 
    @JoinColumn(name = "show_id")
    private Show show;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private double price;
}

