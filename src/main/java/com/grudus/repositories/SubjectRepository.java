package com.grudus.repositories;

import com.grudus.entities.Subject;
import com.grudus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByUser(User user);
    Optional<Subject> findByUserIdAndAndroidId(Long userId, Long androidId);
}
