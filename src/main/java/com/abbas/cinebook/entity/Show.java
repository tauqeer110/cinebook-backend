package com.abbas.cinebook.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "shows")
@Getter 
@Setter 
public class Show {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne 
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @OneToMany(mappedBy = "show")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "show")
    private List<ShowPricing> showPricing;
}
