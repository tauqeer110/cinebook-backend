package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    
}
