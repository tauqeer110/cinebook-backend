package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abbas.cinebook.entity.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Integer> {
    boolean existsByTheaterIdAndScreenNumber(int theaterId, int screenNumber);
}
