package com.grudus.configuration.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grudus.entities.User;
import com.grudus.helpers.exceptions.UserAuthenticationException;
import com.grudus.repositories.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secretToken, UserRepository userRepository) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretToken));
        this.userRepository = userRepository;
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        UserAuthenticationToken authToken = (UserAuthenticationToken) authentication;

        User user = authToken.getUser();

        String token = user.getToken();
        if (token == null || token.trim().isEmpty())
            try {
                token = tokenHandler.createTokenForUser(user);
                userRepository.updateToken(user.getId(), token);
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                e.printStackTrace();
                return;
            }

            response.setHeader(AUTH_HEADER_NAME, token);

    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token == null)
            return null;

        try {
            User user = userRepository.findByToken(token)
                    .orElse(tokenHandler.parseUserFromToken(token));

            if (user == null)
                return null;

            if (user.getToken() == null)
                userRepository.updateToken(user.getId(), token);


            return new UserAuthenticationToken(user);

        } catch (IOException e) {
            throw new UserAuthenticationException("Cannot parse user from token", e);
        }
    }
}
