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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "location"}))
public class Theater {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String location;

    @OneToMany(mappedBy = "theater")
    private List<Screen> screens;
}
