package com.grudus.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grudus.entities.User;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private final TokenHandler tokenHandler;
    private final UserRepository userRepository;


    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secretToken, UserRepository userRepository) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretToken));
        this.userRepository = userRepository;
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        Principal principal = (Principal) token.getPrincipal();

        final User user = userRepository.findByUserName(principal.getName()).orElseThrow(() -> new RuntimeException("Cannot find the user"));
        try {
            response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
            System.err.println("auth " +AUTH_HEADER_NAME + " was added -> " + response.getHeader(AUTH_HEADER_NAME) );
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException("Error");
        }

    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        System.err.println("\n----------");
        System.err.println("all request headers: ");

        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String enUm = enumeration.nextElement();
            System.err.println(enUm + " -> " + request.getHeader(enUm));
        }
        System.err.println("----------\n");

        System.err.println("trying to get authentication ---> token for " + AUTH_HEADER_NAME + " is: " + token);
        if (token != null) {
            try {
                final User user = tokenHandler.parseUserFromToken(token);
                if (user != null)
                    return new UsernamePasswordAuthenticationToken((Principal) user::getUserName, user.getPassword());

            } catch (IOException e) {
                throw new RuntimeException("Cannot parse user from token");
            }
        }
        return null;
    }
}
