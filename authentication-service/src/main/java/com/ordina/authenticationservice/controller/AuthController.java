package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.exception.InvalidPasswordException;
import com.ordina.authenticationservice.security.JwtResponse;
import com.ordina.authenticationservice.security.JwtService;
import com.ordina.authenticationservice.security.PubKeyResponse;
import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserCredentials;
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
        User user = userRepository.findByUsername(credentials.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!credentials.equals(user))
            throw new InvalidPasswordException();

        String token = Jwt.generator()
                .withKey(jwtService.getPrivateKey())
                .withUserId(user.getId())
                .witExpiration(JwtService.TOKEN_EXPIRATION_MINUTES);

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/public")
    public ResponseEntity<PubKeyResponse> getPublicKey() {
        byte[] encodedKey = jwtService.getPublicKey().getEncoded();
        return new ResponseEntity<>(new PubKeyResponse(encodedKey), HttpStatus.OK);
    }
}

