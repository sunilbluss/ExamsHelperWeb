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
    public List<Exam> getAllUserExams(@PathVariable("username") String username, Principal principal) {
        User user = userRepository.findByUsername(username).orElse(User.empty());

        if (principal == null || user.isEmpty() || !(user.getUsername().equals(principal.getName()) || principal.getName().equals("admin")))
            throw new AccessException("You can't see this page");

        return examsRepository.findByUser(user);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllUserExams(@PathVariable("username") String username, Principal principal) {
        if (principal == null || !principal.getName().equals(username))
            return;

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
                        Principal principal) {


        if (principal == null || !principal.getName().equals(username))
            return;

        if (subjectRepository.findOne(subjectID) == null)
            throw new NullPointerException("Subject with id " + subjectID + " doesn't exist");


        examsRepository.save(new Exam(examInfo,
                DateHelper.tryToGetDateFromString(date),
                userRepository.findByUsername(username).get(),
                subjectRepository.findOne(subjectID)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Exam getExam(@PathVariable("username") String username,
                        @PathVariable("id") Long id,
                        Principal principal) {
        if (principal == null || !principal.getName().equals(username))
            return null;

        return userRepository.findByUsername(username)
                .orElse(User.empty())
                .getExams()
                .stream()
                .filter(exam ->  exam.getId() == id)
                .findAny()
                .orElse(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteExam(@PathVariable("username") String username,
                           @PathVariable("id") Long id,
                           Principal principal) {
        if (principal == null || !principal.getName().equals(username))
            return;

        examsRepository.delete(id);
    }

}