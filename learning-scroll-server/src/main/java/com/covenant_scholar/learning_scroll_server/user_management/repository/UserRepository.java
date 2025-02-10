package com.covenant_scholar.learning_scroll_server.user_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.covenant_scholar.learning_scroll_server.user_management.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username); // Add this method for checking duplicate usernames
}
