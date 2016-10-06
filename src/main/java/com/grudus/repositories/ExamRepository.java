package com.grudus.repositories;

import com.grudus.entities.Exam;
import com.grudus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ExamRepository extends JpaRepository<Exam, Long> {

    Optional<Exam> findById(Long id);
    List<Exam> findByUser(User user);
}
