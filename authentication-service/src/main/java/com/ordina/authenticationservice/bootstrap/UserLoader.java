package com.ordina.authenticationservice.bootstrap;

import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserDto;
import com.ordina.authenticationservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        loadUserObjects();
    }

    private synchronized void loadUserObjects() {
        log.debug("Loading initial User objects into the database.");

        // Don't add more users if database is already populated
        if (userRepository.count() != 0)
            return;

        userRepository.save(new User(UserDto.builder()
                .username("Rhiannan Foreman")
                .email("rhiannan.foreman@gmail.com")
                .password("p4ssw0rd")
                .build()));

        userRepository.save(new User(UserDto.builder()
                .username("Darrel Neville")
                .email("darrel.neville@gmail.com")
                .password("1234567890")
                .build()));

        userRepository.save(new User(UserDto.builder()
                .username("Audrey Chan")
                .email("a.chan@outlook.com")
                .password("welcome01")
                .build()));

        userRepository.save(new User(UserDto.builder()
                .username("Alyssia Read")
                .email("alyssiaread@gmail.com")
                .password("98m2^LW*9%3")
                .build()));

        log.debug("User Records loaded: {}", userRepository.count());
    }
}
