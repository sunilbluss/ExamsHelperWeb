package com.grudus.helpers.validation;


import com.grudus.entities.User;
import com.grudus.helpers.exceptions.NewUserException;
import com.grudus.repositories.UserRepository;

import java.util.Date;

public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateInputs(String username, String password, String email, Date date) {
        if (!StringValidator.usernameIsCorrect(username))
            throw new NewUserException("username: " + username + " isn't correct");

        if (!StringValidator.emailIsCorrect(email))
            throw new NewUserException("Email: " + email + " isn't correc");

        if (!StringValidator.passwordIsCorrect(password))
            throw new NewUserException("Password isn't correct");

        if (!DateValidator.dateIsCorrect(date))
            throw new NewUserException("Date: " + date + " isn't correct");
    }

    public boolean userExist(User user) {
        return userExist(user.getUsername());
    }

    public boolean userExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


}
