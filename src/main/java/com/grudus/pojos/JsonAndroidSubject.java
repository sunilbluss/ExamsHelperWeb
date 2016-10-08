package com.grudus.pojos;


public class JsonAndroidSubject {

    private Long id;
    private Long userId;
    private String title;
    private String color;
    private String change;

    public JsonAndroidSubject() {
    }

    public JsonAndroidSubject(Long id, Long userId, String title, String color, String change) {

        this.id = id;
        this.userId = userId;
        this.title = title;
        this.color = color;
        this.change = change;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return "JsonAndroidSubject{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", change=" + change +
                '}';
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }
}
