package com.grudus.helpers;


import com.grudus.entities.User;
import com.grudus.repositories.UserRepository;

import java.util.Date;

public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean inputsAreValid(String userName, String password, String email, Date date) {

        if (!StringValidator.userNameIsCorrect(userName)) {
            System.err.println("Username: " + userName + " isn't correct");
            return false;
        }
        if (!StringValidator.emailIsCorrect(email)) {
            System.err.println("Email: " + email + " isn't correc");
            return false;
        }
        if (!StringValidator.passwordIsCorrect(password)) {
            System.err.println("Password isn't correct");
            return false;
        }
        if (!DateValidator.dateIsCorrect(date)) {
            System.err.println("Date: " + date + " isn't correct");
            return false;
        }
        return true;
    }

    public boolean userExist(User user) {
        return userExist(user.getUserName());
    }

    public boolean userExist(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }


}
