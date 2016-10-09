package com.grudus.controllers;

import com.grudus.configuration.authentication.UserAuthenticationToken;
import com.grudus.entities.Subject;
import com.grudus.helpers.Change;
import com.grudus.helpers.JsonObjectHelper;
import com.grudus.helpers.exceptions.NotFoundException;
import com.grudus.pojos.JsonAndroidSubject;
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
@RequestMapping("/api/user/{username}/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectController(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }



    @RequestMapping(method = RequestMethod.GET)
    public List<JsonAndroidSubject> getAllSubjects(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        return subjectRepository.findAll()
                .stream()
                .map(JsonObjectHelper::subjectObjectToJson)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    public void firstInsert(@PathVariable("username") String username, UserAuthenticationToken currentUser,
                            @RequestBody JsonAndroidSubject[] subjects) {

        if (subjects == null || subjects.length == 0)
            return;

        checkAuthority(currentUser, username);

        System.err.println("get: " + subjects.length + " -> " + Arrays.asList(subjects));


        new ArrayList<>(Arrays.asList(subjects))
                .forEach(jsonSubject -> {
                    String change = jsonSubject.getChange();
                    Subject subject = JsonObjectHelper.subjectJsonToObject(jsonSubject, userRepository);
                    if (change.equals(Change.CREATE.toString())) {
                        subjectRepository.save(subject);
                    }
                    else if (change.equals(Change.DELETE.toString())) {
                        subjectRepository.delete(subject);
                    }
                    else if (change.equals(Change.UPDATE.toString())) {
                        subjectRepository.save(subject);
                    }

                });
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllSubjects(@PathVariable("username") String username, UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        currentUser.getUser()
                .getSubjectList()
                .stream()
                .map(Subject::getId)
                .forEach(subjectRepository::delete);
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addSubject(@PathVariable("username") String username,
                           @RequestBody JsonAndroidSubject jsonSubject,
                           UserAuthenticationToken currentUser) {


        System.err.println("POST: " + jsonSubject);
        checkAuthority(currentUser, username);
        subjectRepository.save(JsonObjectHelper.subjectJsonToObject(jsonSubject, userRepository));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{androidId}")
    public void updateSubject(@PathVariable("username") String username,
                              @PathVariable("androidId") Long androidId,
                              @RequestBody JsonAndroidSubject jsonSubject,
                              UserAuthenticationToken currentUser) {

        checkAuthority(currentUser, username);

        System.err.println("PUT: " + jsonSubject);

        Subject oldSubject = subjectRepository.findByUserIdAndAndroidId(currentUser.getUser().getId(), androidId)
                .orElseThrow(NullPointerException::new);

        oldSubject.setTitle(jsonSubject.getTitle());
        oldSubject.setColor(jsonSubject.getColor());
        oldSubject.setAndroidId(jsonSubject.getId());

        subjectRepository.save(oldSubject);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{androidId}")
    public void deleteSubject(@PathVariable("username") String username,
                              @PathVariable("androidId") Long androidId,
                              @RequestBody JsonAndroidSubject jsonAndroidSubject,
                              UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        Subject subject = subjectRepository.findByUserIdAndAndroidId(jsonAndroidSubject.getUserId(), androidId)
                .orElseThrow(NotFoundException::new);

        subjectRepository.delete(subject.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    public JsonAndroidSubject getSubject(@PathVariable("username") String username,
                           @PathVariable("androidId") Long androidId,
                           UserAuthenticationToken currentUser) {
        checkAuthority(currentUser, username);

        Subject subject = subjectRepository.findByUserIdAndAndroidId(currentUser.getUser().getId(), androidId)
                .orElseThrow(NotFoundException::new);

        return JsonObjectHelper.subjectObjectToJson(subject);
    }


}
