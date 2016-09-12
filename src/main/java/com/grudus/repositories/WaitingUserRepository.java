package com.grudus.repositories;

import com.grudus.entities.WaitingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WaitingUserRepository extends JpaRepository<WaitingUser, String> {
    Optional<WaitingUser> findByKey(String email);
}
