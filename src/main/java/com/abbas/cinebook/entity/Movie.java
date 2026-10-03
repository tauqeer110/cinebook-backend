package com.abbas.cinebook.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title", "duration"}))
public class Movie {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private double duration;

    @OneToMany(mappedBy = "movie")
    private List<Show> shows;
}
