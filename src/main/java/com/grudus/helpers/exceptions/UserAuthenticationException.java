package com.grudus.helpers.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserAuthenticationException extends BadCredentialsException {

    public UserAuthenticationException() {
        super("Access Denied");
    }

    public UserAuthenticationException(String message) {
        super(message);
    }
    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
