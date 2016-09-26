package com.grudus.configuration.authentication;

import com.grudus.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final User user;

    public UserAuthenticationToken(User user) {
        this(user, null);
    }

    public UserAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUserName(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return this.user.getUserName();
    }

    @Override
    public Object getCredentials() {
        return this.getUser().getPassword();
    }

}
