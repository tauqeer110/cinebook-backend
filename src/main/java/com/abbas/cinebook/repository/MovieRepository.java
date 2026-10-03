package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    boolean existsByTitleAndDuration(String title, double duration);
}
