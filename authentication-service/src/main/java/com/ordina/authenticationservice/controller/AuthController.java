package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.exception.WrongPasswordException;
import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserCredentials;
import com.ordina.authenticationservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String authenticate(@RequestBody UserCredentials credentials) {
        User user = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(credentials.getPassword()))
            throw new WrongPasswordException();

        // FIXME: Returns fake token for now
        return "token123";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

}
