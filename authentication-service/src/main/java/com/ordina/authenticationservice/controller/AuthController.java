package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.exception.InvalidPasswordException;
import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserCredentials;
import com.ordina.authenticationservice.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String authenticate(@RequestBody UserCredentials credentials) {
        User user = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(UserNotFoundException::new);

        log.info(user.toString());
        log.info(credentials.toString());

        if (!credentials.equals(user))
            throw new InvalidPasswordException();

        // FIXME: Returns fake token for now
        return "token123";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

}

