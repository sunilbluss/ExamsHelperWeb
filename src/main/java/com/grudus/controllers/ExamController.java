package com.grudus.controllers;

import com.grudus.entities.Exam;
import com.grudus.entities.User;
import com.grudus.helpers.EmailSender;
import com.grudus.helpers.SessionIdentifierGenerator;
import com.grudus.repositories.ExamRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ExamController {

    private final UserRepository userRepository;
    private final ExamRepository examsRepository;
    private final EmailSender emailSender;

    @Autowired
    public ExamController(UserRepository userRepository, ExamRepository examsRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.examsRepository = examsRepository;
        this.emailSender = emailSender;
    }

    @RequestMapping("/{userName}/exams")
    public List<Exam> getAllUserExams(@PathVariable("userName") String userName) {

        User user = userRepository.findByUserName(userName).orElse(User.empty());
        if (user.isEmpty())
            return new ArrayList<>(0);
        return examsRepository.findByUser(user);
    }

}