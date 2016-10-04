package com.grudus.repositories;

import com.grudus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User user SET user.token = :token WHERE user.id = :id")
    int updateToken(@Param("id") long id, @Param("token") String token);
}
