package com.grudus.controllers;

import com.grudus.entities.User;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @RequestMapping(value = "add/{user}")
    public String addUser(@PathVariable("user") String userName) {
        User user = new User(userName, "dupa", "hehe@email.com", Calendar.getInstance().getTime());
        System.err.println("addUser method " + user);
        userRepository.save(user);

        return user + " was added";
    }



}