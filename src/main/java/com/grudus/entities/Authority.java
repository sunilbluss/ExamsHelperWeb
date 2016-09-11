package com.grudus.entities;

import javax.persistence.*;

// TODO: 12.09.16 all this authority stuff smells ;/
@Entity(name = "authorities")
public class Authority {

    @Column(name = "username", nullable = false, unique = true)
    @Id
    private String username;

    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Authority() {
    }

    public Authority(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
