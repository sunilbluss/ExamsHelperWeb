package com.grudus.controllers;

import com.grudus.entities.Authority;
import com.grudus.entities.Role;
import com.grudus.entities.User;
import com.grudus.helpers.exceptions.NewUserException;
import com.grudus.helpers.validation.UserValidator;
import com.grudus.repositories.AuthorityRepository;
import com.grudus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
    public User getUser(@PathVariable("username") String userName, Principal principal) {
        if (principal == null || !principal.getName().equals(userName))
            return User.empty();

        return userRepository.findByUserName(userName).orElse(User.empty());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        authorityRepository.deleteAll();
        userRepository.deleteAll();
        return "All records were deleted "  + userRepository.count();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addUser(@RequestParam("username") String userName, @RequestParam("password") String password,
                        @RequestParam("email") String email) {


        // TODO: 11.09.16 debug only  -----------------------------
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 4, 4);
        // --------------------------------------------------------

        UserValidator validator = new UserValidator(userRepository);

        validator.validateInputs(userName, password, email, calendar.getTime());

        if (validator.userExist(userName))
            throw new NewUserException("User exist");

        userRepository.save(new User(userName, passwordEncoder.encode(password), email, calendar.getTime()));
        authorityRepository.save(new Authority(userName, Role.ROLE_USER));
    }



}