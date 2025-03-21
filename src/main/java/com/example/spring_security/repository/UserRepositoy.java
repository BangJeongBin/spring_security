package com.example.spring_security.repository;

import com.example.spring_security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoy extends JpaRepository<User, Integer> {
    Optional<User> findByUserid(String usernid);
    boolean existsByUserid(String userid);
    boolean existsByEmail(String email);
}
