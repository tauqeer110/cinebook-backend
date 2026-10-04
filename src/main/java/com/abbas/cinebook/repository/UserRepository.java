package com.abbas.cinebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abbas.cinebook.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
