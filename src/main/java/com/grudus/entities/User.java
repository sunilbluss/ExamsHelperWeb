package com.grudus.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "users")
public class User {

    private static final User EMPTY = new User(null, null, null, null, null);

    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;

    @Column(length = 32, nullable = false, name = "username")
    private String userName;

    @Column(length = 64, name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "register_date")
    private Date date;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Exam> exams;

    public User() {}

    public User(String userName, String password, String email, Date date, Collection<Exam> exams) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.date = date;
        this.exams = exams;
    }

    public User(String userName, String password, String email, Date date) {
        this(userName, password, email, date, new ArrayList<>(0));
    }

    public Collection<Exam> getExams() {
        return exams;
    }

    public void setExams(Collection<Exam> exams) {
        this.exams = exams;
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