package com.grudus.helpers.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Can't find user");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
