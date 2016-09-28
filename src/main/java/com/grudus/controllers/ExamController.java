package com.grudus.controllers;

import com.grudus.entities.Exam;
import com.grudus.entities.User;
import com.grudus.helpers.DateHelper;
import com.grudus.helpers.exceptions.AccessException;
import com.grudus.repositories.ExamRepository;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class ExamController {

    private final UserRepository userRepository;
    private final ExamRepository examsRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ExamController(UserRepository userRepository, ExamRepository examsRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.examsRepository = examsRepository;
        this.subjectRepository = subjectRepository;
    }

    @RequestMapping("/{username}/exams")
    public List<Exam> getAllUserExams(@PathVariable("username") String username, Principal principal) {
        User user = userRepository.findByUsername(username).orElse(User.empty());

        if (principal == null || user.isEmpty() || !(user.getUsername().equals(principal.getName()) || principal.getName().equals("admin")))
            throw new AccessException("You can't see this page");

        return examsRepository.findByUser(user);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{username}/exams/add")
    public void addExam(@PathVariable("username") String username,
                        @RequestParam("subject") long subjectID,
                        @RequestParam("info") String examInfo,
                        @RequestParam("date") String date,
                        Principal principal) {

        User user = userRepository.findByUsername(username).orElse(User.empty());

        if (user.isEmpty() || principal == null || !principal.getName().equals(user.getUsername()))
            throw new AccessException("Cannot access this page");

        if (subjectRepository.findOne(subjectID) == null)
            throw new NullPointerException("Subject with id " + subjectID + " doesn't exist");

        examsRepository.save(new Exam(examInfo,
                DateHelper.tryToGetDateFromString(date),
                user,
                subjectRepository.findOne(subjectID)));
    }

}