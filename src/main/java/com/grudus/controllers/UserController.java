package com.grudus.controllers;

import com.grudus.entities.Role;
import com.grudus.entities.User;
import com.grudus.entities.WaitingUser;
import com.grudus.helpers.EmailSender;
import com.grudus.helpers.exceptions.NewUserException;
import com.grudus.helpers.exceptions.UserAuthenticationException;
import com.grudus.helpers.validation.UserValidator;
import com.grudus.repositories.UserRepository;
import com.grudus.repositories.WaitingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final WaitingUserRepository waitingUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    @Autowired
    public UserController(UserRepository userRepository, WaitingUserRepository waitingUserRepository, PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.waitingUserRepository = waitingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public User getUser(@PathVariable("username") String username, Principal principal) {
        if (principal == null || !principal.getName().equals(username)) {
            return User.empty();
        }

        return userRepository.findByUsername(username).orElse(User.empty());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    public void editUser(@PathVariable("username") String username,
                         @RequestParam("username") String newUsername,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         Principal principal) {
        if (principal == null || !principal.getName().equals(username))
            throw new UserAuthenticationException("You aren't a principal!");

        User user = userRepository.findByUsername(username).orElse(User.empty());

        if (user.isEmpty())
            throw new UserAuthenticationException("User is empty!");

        user.setUsername(newUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    public void deleteUser(@PathVariable("username") String username,
                           Principal principal) {
        if (principal == null || !principal.getName().equals(username))
            throw new UserAuthenticationException("You are not the principal!");

        User user = userRepository.findByUsername(username).orElse(User.empty());

        System.err.println("delete user: " + user);

        userRepository.delete(user.getId());
    }


    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addUserToWaitingRoom(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("email") String email) {


        Date registerDate = Calendar.getInstance().getTime();

        UserValidator validator = new UserValidator(userRepository);

        validator.validateInputs(username, password, email, registerDate);

        if (validator.userExist(username))
            throw new NewUserException("User exist");

        String key = "";
        try {
            key = emailSender.sendKeyMessageAndGetKey(username, email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        waitingUserRepository.save(new WaitingUser(username, passwordEncoder.encode(password), email, key));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/add/{username}/{key}")
    public String addUser(@PathVariable("username") String username, @PathVariable("key") String key) {

        WaitingUser user = waitingUserRepository.findByKey(key)
                .orElseThrow(() -> new NewUserException("You are not a waiting user!"));

        if (!user.getUsername().equals(username))
            throw new NewUserException("Your username isn't correct!");

        userRepository.save(new User(username, user.getPassword(), user.getEmail(), user.getDate(), Role.ROLE_USER));
        waitingUserRepository.delete(username);

        return username + " was successfully registered";
    }
}

