package com.grudus.helpers.validation;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.Date;

public class StringValidator {

    public static boolean usernameIsCorrect(String username) {
        return username.trim().length() > 0 && username.indexOf(';') < 0;
    }

    public static boolean passwordIsCorrect(String password) {
        return password.length() > 8 && password.length() < 64;
    }

    public static boolean emailIsCorrect(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

}
