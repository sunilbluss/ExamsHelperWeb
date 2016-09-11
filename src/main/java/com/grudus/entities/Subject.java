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

    @Column(nullable = false, length = 64)
    private String title;

    @Column(length = 9)
    private String color;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<Exam> exams;

    public Subject(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public Subject() {
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}