package com.grudus.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exams")
public class Exam {

    @GeneratedValue
    @Id
    @Column
    private long id;

    @Column(name = "android_id")
    private Long androidId;

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

    public Exam(Long androidId, String examInfo, Date date, User user, Subject subject) {
        this.androidId = androidId;
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

    public Long getAndroidId() {
        return androidId;
    }

    public void setAndroidId(Long androidId) {
        this.androidId = androidId;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", androidId=" + androidId +
                ", examInfo='" + examInfo + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", subject=" + subject +
                '}';
    }

    public static Exam empty() {
        return new Exam(-1L, "", null, User.empty(), Subject.empty());
    }
}