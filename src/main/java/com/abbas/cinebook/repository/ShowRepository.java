package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Integer> {
    
}
