package com.grudus.configuration;

import com.grudus.entities.User;
import com.grudus.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;


public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {


    private TokenAuthenticationService tokenAuthenticationService;
    private UserRepository userRepository;

    protected StatelessLoginFilter(String defaultFilterProcessesUrl, TokenAuthenticationService tokenAuthenticationService, UserRepository userRepository) {
        super(defaultFilterProcessesUrl);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        System.err.println("Attempt to authentication");
        return new UsernamePasswordAuthenticationToken((Principal) () -> "username", "password1");


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.err.println("successfull auth " + authResult.getName());
        User authenticatedUser = userRepository.findByUserName(authResult.getName()).orElseThrow(() -> new RuntimeException("NIE MA " + authResult.getName()));
        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken((Principal) authenticatedUser::getUserName, authenticatedUser.getPassword());

        tokenAuthenticationService.addAuthentication(response, auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
