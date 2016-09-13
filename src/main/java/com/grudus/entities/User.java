package com.grudus.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
public class User {

    private static final User EMPTY = new User(null, null, null, null, null, null);

    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;

    @Column(length = 32, nullable = false, name = "username")
    private String userName;

    @Column(length = 64, name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "register_date")
    private Date date;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Exam> exams;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Subject> subjectList;

    public User() {}

    public User(String userName, String password, String email, Date date, Collection<Exam> exams, List<Subject> subjectList) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.date = date;
        this.exams = exams;
        this.subjectList = subjectList;
        this.enabled = true;
    }

    public User(String userName, String password, String email, Date date) {
        this(userName, password, email, date, new ArrayList<>(0), new ArrayList<>(0));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Exam> getExams() {
        return exams;
    }

    public void setExams(Collection<Exam> exams) {
        this.exams = exams;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public static User empty() {
        return EMPTY;
    }

    public boolean isEmpty() {
        return userName == null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }

}