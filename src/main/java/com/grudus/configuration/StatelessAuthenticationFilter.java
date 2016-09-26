package com.grudus.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;


public class StatelessAuthenticationFilter extends OncePerRequestFilter {

    private final TokenAuthenticationService authenticationService;

    public StatelessAuthenticationFilter(TokenAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.err.println("start do filter method AuthenticationFilter");
        Authentication auth =  authenticationService.getAuthentication(request);
        System.err.println("doFilterInternal: auth is:" + auth);
        System.err.println("principal: " + ((Principal)auth.getPrincipal()).getName() + " , name=" + auth.getName() + ", creditals="  + auth.getCredentials() );
        if (auth != null)
            SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
