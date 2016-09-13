package com.grudus.repositories;

import com.grudus.entities.Subject;
import com.grudus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByUser(User user);
}
