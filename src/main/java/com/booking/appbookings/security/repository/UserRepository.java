package com.booking.appbookings.security.repository;

import com.booking.appbookings.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String userName);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
}
