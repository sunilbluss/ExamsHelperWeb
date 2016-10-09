package com.grudus.helpers.exceptions;


public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Cannot find in the database");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
