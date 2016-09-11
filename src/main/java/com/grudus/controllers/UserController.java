package com.grudus.controllers;

import com.grudus.entities.User;
import com.grudus.helpers.UserValidator;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public User getUser(@PathVariable("username") String userName) {
        return userRepository.findByUserName(userName).orElse(User.empty());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        userRepository.deleteAll();
        return "All records were deleted "  + userRepository.count();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addUser(@RequestParam("username") String userName, @RequestParam("password") String password,
                        @RequestParam("email") String email) {

        // TODO: 11.09.16 debug only
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 4, 4);

        UserValidator validator = new UserValidator(userRepository);
        if (!validator.inputsAreValid(userName, password, email, calendar.getTime()))
            return;

        if (validator.userExist(userName))
            throw new RuntimeException("User exist");

        userRepository.save(new User(userName, passwordEncoder.encode(password), email, calendar.getTime()));

    }



}