package com.grudus.controllers;

import com.grudus.configuration.authentication.UserAuthenticationToken;
import com.grudus.entities.Exam;
import com.grudus.entities.User;
import com.grudus.helpers.AuthenticationHelper;
import com.grudus.helpers.DateHelper;
import com.grudus.helpers.exceptions.UserAuthenticationException;
import com.grudus.helpers.exceptions.UserNotFoundException;
import com.grudus.repositories.ExamRepository;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.grudus.helpers.AuthenticationHelper.checkAuthority;

// TODO: 28.09.16 delete username from url

@RestController
@RequestMapping("/api/user/{username}/exams")
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

    @RequestMapping(method = RequestMethod.GET)
    public List<Exam> getAllUserExams(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new)
                .getExams()
                .stream()
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllUserExams(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        userRepository.findByUsername(username)
                .orElse(User.empty())
                .getExams()
                .stream()
                .map(Exam::getId)
                .forEach(examsRepository::delete);
    }


    @RequestMapping(method = RequestMethod.POST)
    public void addExam(@PathVariable("username") String username,
                        @RequestParam("subject") long subjectID,
                        @RequestParam("info") String examInfo,
                        @RequestParam("date") String date,
                        UserAuthenticationToken currentUser) {

        checkAuthority(currentUser, username);

        if (subjectRepository.findOne(subjectID) == null)
            throw new NullPointerException("Subject with id " + subjectID + " doesn't exist");


        examsRepository.save(new Exam(
                examInfo,
                DateHelper.tryToGetDateFromString(date),
                userRepository.findByUsername(username).get(),
                subjectRepository.findOne(subjectID)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Exam getExam(@PathVariable("username") String username,
                        @PathVariable("id") Long id,
                        UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        return userRepository.findByUsername(username)
                .orElse(User.empty())
                .getExams()
                .stream()
                .filter(exam ->  exam.getId() == id)
                .findAny()
                .orElseThrow(NullPointerException::new);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteExam(@PathVariable("username") String username,
                           @PathVariable("id") Long id,
                           UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        examsRepository.delete(id);
    }

}