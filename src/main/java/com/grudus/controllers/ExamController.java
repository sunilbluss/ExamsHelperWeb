package com.grudus.controllers;

import com.grudus.entities.Exam;
import com.grudus.entities.User;
import com.grudus.repositories.ExamRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ExamController {

    private final UserRepository userRepository;
    private final ExamRepository examsRepository;

    @Autowired
    public ExamController(UserRepository userRepository, ExamRepository examsRepository) {
        this.userRepository = userRepository;
        this.examsRepository = examsRepository;
    }

    @RequestMapping("/{userName}/exams")
    public List<Exam> getAllUserExams(@PathVariable("userName") String userName) {

        User user = userRepository.findByUserName(userName).orElse(User.empty());
        if (user.isEmpty())
            return new ArrayList<>(0);
        return examsRepository.findByUser(user);
    }
}