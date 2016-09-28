package com.grudus.controllers;

import com.grudus.entities.Subject;
import com.grudus.entities.User;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/api/user/{username}/subjects/add", method = RequestMethod.POST)
    public void addSubject(@PathVariable("username") String username,
                           @RequestParam("subject") String subjectTitle,
                           @RequestParam("color") String color) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("Cannot find the user " + username));

        if (user.getSubjectList()
                .stream()
                .map(Subject::getTitle)
                .anyMatch(title -> title.equals(subjectTitle)))
            throw new RuntimeException("subject already exist");

        subjectRepository.save(new Subject(subjectTitle, "#" + color, user));

    }

    @RequestMapping("/api/user/{username}/subjects/deleteAll")
    public void deleteAllSubjects(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NullPointerException("Cannot find the user " + username));

        user.getSubjectList()
                .stream()
                .map(Subject::getId)
                .forEach(subjectRepository::delete);

    }
}
