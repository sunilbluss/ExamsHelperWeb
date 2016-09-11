package com.grudus.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "exams")
public class Exam {

    @GeneratedValue
    @Id
    @Column
    private long id;

    @Column(length = 128, name = "info")
    private String examInfo;

    @Column
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public Exam() {
    }

    public Exam(String examInfo, Date date, User user, Subject subject) {
        this.examInfo = examInfo;
        this.date = date;
        this.user = user;
        this.subject = subject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(String examInfo) {
        this.examInfo = examInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", subject=" + subject.getTitle() +
                ", examInfo='" + examInfo + '\'' +
                ", date=" + date +
                ", user=" + user.getUserName() +
                '}';
    }
}