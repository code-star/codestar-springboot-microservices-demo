package com.ordina.authenticationservice.controller;

import com.ordina.authenticationservice.controller.dto.UserDto;
import com.ordina.authenticationservice.controller.dto.UserResponse;
import com.ordina.authenticationservice.exception.UserNotFoundException;
import com.ordina.authenticationservice.model.UserDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/user")
public class UserController {

    private final UserDtoRepository userRepository;

    // TODO: remove, is for debugging
    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::toUserResponse)
                .toList();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserDetails(@PathVariable String userId) {
        UserDto userDto = userRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(UserNotFoundException::new);
        return userDto.toUserResponse();
    }

//    @GetMapping("/me")
//    public UserResponse getCurrentUserDetails() {
//        UserDto userDto = userRepository.findByUserId(UUID.fromString(userId))
//                .orElseThrow(UserNotFoundException::new);
//        return userDto.toUserResponse();
//    }
}
