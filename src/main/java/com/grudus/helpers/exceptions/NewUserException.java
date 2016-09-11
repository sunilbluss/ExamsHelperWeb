package com.grudus.helpers.exceptions;


public class NewUserException extends RuntimeException {

    public NewUserException() {
    }

    public NewUserException(String message) {
        super(message);
    }

    public NewUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewUserException(Throwable cause) {
        super(cause);
    }

    public NewUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
