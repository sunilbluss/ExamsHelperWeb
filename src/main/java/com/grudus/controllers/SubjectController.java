package com.grudus.controllers;

import com.grudus.configuration.authentication.UserAuthenticationToken;
import com.grudus.entities.Subject;
import com.grudus.entities.User;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.grudus.helpers.AuthenticationHelper.checkAuthority;

@RestController
@RequestMapping("/api/user/{username}/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addSubject(@PathVariable("username") String username,
                           @RequestParam("subject") String subjectTitle,
                           @RequestParam("color") String color,
                           UserAuthenticationToken currentUser) {

        checkAuthority(currentUser, username);
        User user = currentUser.getUser();

        if (user.getSubjectList()
                .stream()
                .map(Subject::getTitle)
                .anyMatch(title -> title.equals(subjectTitle)))
            throw new RuntimeException("subject already exist");

        subjectRepository.save(new Subject(subjectTitle, "#" + color, user));

    }

    @RequestMapping("/deleteAll")
    public void deleteAllSubjects(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        currentUser.getUser()
                .getSubjectList()
                .stream()
                .map(Subject::getId)
                .forEach(subjectRepository::delete);
    }
}
