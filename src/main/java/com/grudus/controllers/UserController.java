package com.grudus.controllers;

import com.grudus.entities.Authority;
import com.grudus.entities.Role;
import com.grudus.entities.User;
import com.grudus.entities.WaitingUser;
import com.grudus.helpers.EmailSender;
import com.grudus.helpers.exceptions.NewUserException;
import com.grudus.helpers.validation.UserValidator;
import com.grudus.repositories.AuthorityRepository;
import com.grudus.repositories.UserRepository;
import com.grudus.repositories.WaitingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final WaitingUserRepository waitingUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final EmailSender emailSender;

    @Autowired
    public UserController(UserRepository userRepository, WaitingUserRepository waitingUserRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.waitingUserRepository = waitingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.emailSender = emailSender;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/{username}")
    public User getUser(@PathVariable("username") String userName, Principal principal) {
        if (principal == null || !principal.getName().equals(userName))
            return User.empty();

        return userRepository.findByUserName(userName).orElse(User.empty());
    }

    @RequestMapping(value = "/api/user")
    public User getUser(@RequestParam(name = "username", required = false) String userName,
                        @RequestParam(name = "id", required = false) String id,
                        Principal principal) {
        if (userName == null && id == null)
            return User.empty();

        if (!id.matches("\\d+"))
            return User.empty();

        User user;

        if (userName == null) {
            user = userRepository.findOne(Long.valueOf(id));
            if (user == null)
                user = User.empty();
        }

        else
            user = userRepository.findByUserName(userName).orElse(User.empty());

        if (!principal.getName().equals(user.getUserName()))
            return User.empty();

        return user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/api/admin/deleteAll")
    public String deleteAll() {
        authorityRepository.deleteAll();
        userRepository.deleteAll();
        return "All records were deleted "  + userRepository.count();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/api/add")
    public void addUserToWaitingRoom(@RequestParam("username") String userName, @RequestParam("password") String password,
                                     @RequestParam("email") String email) {


        Date registerDate = Calendar.getInstance().getTime();

        UserValidator validator = new UserValidator(userRepository);

        validator.validateInputs(userName, password, email, registerDate);

        if (validator.userExist(userName))
            throw new NewUserException("User exist");

        String key = "";
        try {
            key = emailSender.sendKeyMessageAndGetKey(userName, email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        waitingUserRepository.save(new WaitingUser(userName, passwordEncoder.encode(password), email, key));
    }


    // TODO: 16.09.16 delete - method checks if android request call was reached
    @RequestMapping(method = RequestMethod.POST, value = "/post")
    public String doPost(@RequestParam(name = "username", required = false) String username) {
        System.err.println("in post method");
        System.err.println("username: (" + username + ")");

        return "after post";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/add/{username}/{key}")
    public String addUser(@PathVariable("username") String username, @PathVariable("key") String key) {

        WaitingUser user = waitingUserRepository.findByKey(key)
                .orElseThrow(() -> new NewUserException("You are not a waiting user!"));

        if (!user.getUserName().equals(username))
            throw new NewUserException("Your username isn't correct!");

        userRepository.save(new User(username, user.getPassword(), user.getEmail(), user.getDate()));
        authorityRepository.save(new Authority(username, Role.ROLE_USER));
        waitingUserRepository.delete(username);

        return username + " was successfully registered";
    }



}