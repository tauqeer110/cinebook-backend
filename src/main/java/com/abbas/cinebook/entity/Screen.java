package com.abbas.cinebook.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class Screen {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private int screen_number;
    
    @ManyToOne 
    @JoinColumn(name = "theater_id")
    Theater theater;
    
    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;

    @OneToMany(mappedBy = "screen")
    private List<Show> shows;

}
