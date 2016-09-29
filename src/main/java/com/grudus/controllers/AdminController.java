package com.grudus.controllers;

import com.grudus.entities.Subject;
import com.grudus.entities.User;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public AdminController(UserRepository userRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @RequestMapping("/subjects")
    public List<Subject> getAllSubjects(@RequestParam(name = "user", required = false) String username) {
        if (username != null)
            return subjectRepository.findByUser(userRepository.findByUsername(username).orElse(User.empty()));
        return subjectRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        userRepository.deleteAll();
        return "All records were deleted "  + userRepository.count();
    }
}
