package com.grudus.configuration.authentication;

import com.grudus.entities.User;
import com.grudus.helpers.exceptions.UserAuthenticationException;
import com.grudus.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserAuthenticationProvider(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (username == null || credentials == null)
            throw new UserAuthenticationException("Username or password is null");

        final String password = (authentication.getCredentials().toString().trim());
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserAuthenticationException("Cannot find user: " + username));

        if (!encoder.matches(password, user.getPassword()))
            throw new UserAuthenticationException("Passwords are not equals");

        // TODO: 26.09.16 get authentication from user
        return new UserAuthenticationToken(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
