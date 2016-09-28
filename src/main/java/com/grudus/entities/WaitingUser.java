package com.grudus.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;

@Entity(name = "waiting_users")
public class WaitingUser {

    @Id
    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "register_key")
    private String key;

    @Column(name = "register_date")
    private Date date;

    public WaitingUser() {
    }

    public WaitingUser(String username, String password, String email, String key) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.key = key;
        this.date = Calendar.getInstance().getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "WaitingUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
