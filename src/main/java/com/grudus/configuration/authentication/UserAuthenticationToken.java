package com.grudus.configuration.authentication;

import com.grudus.entities.Role;
import com.grudus.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;


public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final User user;

    public UserAuthenticationToken(User user) {
        this(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())));
    }

    public UserAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAdmin() {
        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(Role.ROLE_ADMIN.toString()));
    }

    @Override
    public Object getPrincipal() {
        return this.user.getUsername();
    }

    @Override
    public Object getCredentials() {
        return this.getUser().getPassword();
    }

    @Override
    public String toString() {
        return user.toString();
    }
}
