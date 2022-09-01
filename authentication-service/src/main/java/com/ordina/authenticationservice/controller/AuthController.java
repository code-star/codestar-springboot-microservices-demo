package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.controller.dto.UserCredentials;
import com.ordina.authenticationservice.controller.dto.UserDto;
import com.ordina.authenticationservice.controller.dto.UserRequest;
import com.ordina.authenticationservice.exception.InvalidPasswordException;
import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.model.UserDtoRepository;
import com.ordina.authenticationservice.security.JwtResponse;
import com.ordina.authenticationservice.security.PubKeyResponse;
import com.ordina.jwtauthlib.host.JwtHostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserDtoRepository userRepository;
    private final JwtHostService jwtHostService;

    @PostMapping
    public JwtResponse authenticate(@RequestBody UserCredentials credentials) {
        UserDto user = userRepository.findByUsername(credentials.username())
                .orElseThrow(UserNotFoundException::new);

        if (!user.passwordEquals(credentials.password()))
            throw new InvalidPasswordException();

        return JwtResponse.builder()
                .userId(user.getId().toString())
                .token(jwtHostService.getAccessTokenForUser(user.getId()).token())
                .build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse register(@RequestBody UserRequest userRequest) {
        UserDto userDto = userRepository.save(userRequest);

        return JwtResponse.builder()
                .userId(userDto.getId().toString())
                .token(jwtHostService.getAccessTokenForUser(userDto.getId()).token())
                .build();
    }

    @GetMapping("/public")
    public PubKeyResponse getPublicKey() {
        byte[] encodedKey = jwtHostService.getPublicKey().getEncoded();
        return new PubKeyResponse(encodedKey);
    }
}
