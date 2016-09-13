package com.grudus.controllers;

import com.grudus.entities.Exam;
import com.grudus.entities.Subject;
import com.grudus.entities.User;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping("/subjects")
    public List<Subject> getAllSubjects(@RequestParam(name = "user", required = false) String userName) {
        if (userName != null)
            return subjectRepository.findByUser(userRepository.findByUserName(userName).orElse(User.empty()));
        return subjectRepository.findAll();
    }

    // TODO: 13.09.16 change to post and another url and all
    @RequestMapping("/subject/add")
    public void addSubject(@RequestParam("user") String userName,
                           @RequestParam("subject") String subjectTitle,
                           @RequestParam("color") String color) {

        User user = userRepository.findByUserName(userName).orElseThrow(() -> new NullPointerException("Cannot find the user " + userName));

        if (user.getSubjectList()
                .stream()
                .map(Subject::getTitle)
                .anyMatch(title -> title.equals(subjectTitle)))
            throw new RuntimeException("subject already exist");

        subjectRepository.save(new Subject(subjectTitle, "#" + color, user));

    }

    @RequestMapping("/subject/{username}/deleteAll")
    public void deleteAllMessages(@PathVariable("username") String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new NullPointerException("Cannot find the user " + userName));

        user.getSubjectList()
                .stream()
                .map(Subject::getId)
                .forEach(subjectRepository::delete);

    }
}
