package com.grudus.helpers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.grudus.entities.Exam;
import com.grudus.entities.Subject;
import com.grudus.entities.User;
import com.grudus.pojos.JsonAndroidExam;
import com.grudus.pojos.JsonAndroidSubject;
import com.grudus.repositories.SubjectRepository;
import com.grudus.repositories.UserRepository;
import com.sun.istack.internal.NotNull;

import java.io.IOException;

public class JsonObjectHelper {

    public static JsonAndroidExam examObjectToJson(Exam exam) {
        return new JsonAndroidExam(
                exam.getAndroidId(),
                exam.getSubject().getId(),
                exam.getUser().getId(),
                exam.getExamInfo(),
                exam.getDate());
    }

    public static Exam jsonExamToObject(JsonAndroidExam json, @NotNull SubjectRepository subjectRepository) {
        if (subjectRepository == null)
            return Exam.empty();

        Subject subject = subjectRepository.findOne(json.getSubjectId());

        if (subject == null)
            return Exam.empty();

        return new Exam(
                json.getExamInfo(),
                json.getDate(),
                subject.getUser(),
                subject);
    }

    public static JsonAndroidSubject subjectObjectToJson(Subject subject) {
        return new JsonAndroidSubject(
                subject.getAndroidId(),
                subject.getUser().getId(),
                subject.getTitle(),
                subject.getColor(),
                null
        );
    }

    public static Subject subjectJsonToObject(@NotNull JsonAndroidSubject json, @NotNull UserRepository userRepository) {
        User user = userRepository.findOne(json.getUserId());

        if (user == null)
            return Subject.empty();

        Subject newSubject = new Subject(
                json.getId(),
                json.getTitle(),
                json.getColor(),
                userRepository.findOne(json.getUserId())
        );

        Subject oldSubject =  user.getSubjectList()
                .stream()
                .filter(subject -> subject.getAndroidId().equals(json.getId()))
                .findAny()
                .orElse(Subject.empty());

        if (!json.getChange().equals(Change.CREATE.toString()))
            newSubject.setId(oldSubject.getId());

        return newSubject;

    }
    public static <T> T fromStringJsonToObject(String json, Class<T> type) {
        System.err.println("json: " + json + ", type: " + type.getSimpleName());
        try {
            return new ObjectMapper().readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("Null but why?");
        return null;

    }
}
