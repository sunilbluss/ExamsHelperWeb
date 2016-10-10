package com.grudus.controllers;

import com.grudus.configuration.authentication.UserAuthenticationToken;
import com.grudus.entities.Exam;
import com.grudus.entities.User;
import com.grudus.helpers.Change;
import com.grudus.helpers.JsonObjectHelper;
import com.grudus.helpers.exceptions.UserNotFoundException;
import com.grudus.pojos.JsonAndroidExam;
import com.grudus.repositories.ExamRepository;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.grudus.helpers.AuthenticationHelper.checkAuthority;


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
    public List<JsonAndroidExam> getAllUserExams(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        List<JsonAndroidExam> list =  userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new)
                .getExams()
                .stream()
                .map(JsonObjectHelper::examObjectToJson)
                .collect(Collectors.toList());

        System.err.println("SEND GET: " + list);
        return list;
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
    public void firstInsert(@PathVariable("username") String username,
                            @RequestBody JsonAndroidExam[] exams,
                            UserAuthenticationToken currentUser) {
        if (exams == null || exams.length == 0)
            return;

        checkAuthority(currentUser, username);

        System.err.println("get: " + exams.length + " -> " + Arrays.asList(exams));


        new ArrayList<>(Arrays.asList(exams))
                .forEach(jsonExam -> {
                    Exam exam = JsonObjectHelper.jsonExamToObject(jsonExam, subjectRepository);
                    System.err.println("have: " + exam);
                    String change = jsonExam.getChange();

                    if (change.equals(Change.CREATE.toString())) {
                        if (examsRepository.findByUserIdAndAndroidId(jsonExam.getUserId(), jsonExam.getId()).isPresent()) {
                            System.err.println(exam.getSubject() + ", " + exam.getExamInfo() + " already exist");
                            return;
                        }
                        examsRepository.save(exam);
                    }
                    else
                        throw new UnsupportedOperationException("For now you can only create new exam");
                });


    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addExam(@PathVariable("username") String username,
                        @RequestBody JsonAndroidExam jsonExam,
                        UserAuthenticationToken currentUser) {

        checkAuthority(currentUser, username);

        if (subjectRepository.findOne(jsonExam.getSubjectId()) == null)
            throw new NullPointerException("Subject with id " + jsonExam.getSubjectId() + " doesn't exist");


        examsRepository.save(
                JsonObjectHelper.jsonExamToObject(jsonExam, subjectRepository)
        );
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public JsonAndroidExam getExam(@PathVariable("username") String username,
                        @PathVariable("id") Long id,
                        UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        return examsRepository.findById(id)
                .map(JsonObjectHelper::examObjectToJson)
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