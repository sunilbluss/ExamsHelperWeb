package com.grudus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity(name = "subjects")
public class Subject {

    @GeneratedValue
    @Id
    @Column
    private long id;

    @Column(name = "android_id")
    private Long androidId;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(length = 9)
    private String color;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<Exam> exams;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Subject(Long androidId, String title, String color, User user) {
        this.androidId = androidId;
        this.title = title;
        this.color = color;
        this.user = user;
    }

    public Subject(String title, String color, User user) {
        this.title = title;
        this.color = color;
        this.user = user;
    }

    public Subject() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Subject{" +
//                "id=" + id +
                "android_id= " + androidId +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", user= " + (user == null ? null : user.getUsername()) +
                '}';
    }

    public Long getAndroidId() {
        return androidId;
    }

    public void setAndroidId(Long androidId) {
        this.androidId = androidId;
    }

    public static Subject empty() {
        return new Subject("", "", User.empty());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Subject))
            return false;

        Subject sub = (Subject) obj;

        return sub.getUser().getId() == this.getUser().getId()
                && sub.getTitle().equals(this.getTitle());
    }


}
