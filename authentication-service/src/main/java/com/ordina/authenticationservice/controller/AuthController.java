package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.exception.UserAlreadyExistsException;
import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.exception.InvalidPasswordException;
import com.ordina.authenticationservice.security.JwtResponse;
import com.ordina.authenticationservice.security.JwtService;
import com.ordina.authenticationservice.security.PubKeyResponse;
import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserCredentials;
import com.ordina.authenticationservice.user.UserDto;
import com.ordina.authenticationservice.user.UserRepository;
import com.ordina.jwtauthlib.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<JwtResponse> authenticate(@RequestBody UserCredentials credentials) {
        UserDto user = userRepository.findUserDtoByUsername(credentials.getUsername())
                .orElseThrow(UserNotFoundException::new);

        log.info("Authenticating user: " + user);

        if (!user.hasPassword(credentials.getPassword()))
            throw new InvalidPasswordException();

        log.info("User with id " + user.getId() + " has authenticated succesfully.");

        String token = Jwt.generator()
                .withKey(jwtService.getPrivateKey())
                .withUserId(user.getId())
                .witExpiration(JwtService.TOKEN_EXPIRATION_MINUTES);

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody UserDto userDto) {
        userRepository.findUserDtoByUsername(userDto.getUsername())
                .ifPresent(dto -> {throw new UserAlreadyExistsException();});

        userRepository.findUserDtoByEmail(userDto.getEmail())
                .ifPresent(dto -> {throw new UserAlreadyExistsException();});

        userRepository.save(new User(userDto));
        return userDto;
    }

    @GetMapping("/public")
    public ResponseEntity<PubKeyResponse> getPublicKey() {
        byte[] encodedKey = jwtService.getPublicKey().getEncoded();
        return new ResponseEntity<>(new PubKeyResponse(encodedKey), HttpStatus.OK);
    }
}

