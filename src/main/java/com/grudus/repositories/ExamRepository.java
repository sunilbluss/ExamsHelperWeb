package com.grudus.repositories;

import com.grudus.entities.Exam;
import com.grudus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByUser(User user);
}
