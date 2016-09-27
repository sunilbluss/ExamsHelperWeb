package com.grudus.configuration.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grudus.entities.User;
import com.grudus.helpers.exceptions.UserAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private final TokenHandler tokenHandler;


    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secretToken) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretToken));
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        UserAuthenticationToken authToken = (UserAuthenticationToken) authentication;

        User user = authToken.getUser();

        try {
            response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException("Error");
        }

    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token == null)
            return null;

        try {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null)
                return new UserAuthenticationToken(user);

        } catch (IOException e) {
            throw new UserAuthenticationException("Cannot parse user from token", e);
        }
        return null;
    }
}
