package com.ordina.authenticationservice.bootstrap;

import com.ordina.authenticationservice.controller.dto.UserDto;
import com.ordina.authenticationservice.model.UserDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final UserDtoRepository repository;

    @Override
    public void run(String... args) {
        loadUserObjects();
    }

    private synchronized void loadUserObjects() {
        log.debug("Loading initial User objects into the database.");

        // Don't add more users if database is already populated
        if (repository.count() != 0)
            return;

        repository.save(UserDto.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .email("admin@admin.com")
                .password("admin")
                .enabled(true)
                .build());

        repository.save(UserDto.builder()
                .id(UUID.randomUUID())
                .username("Rhiannan Foreman")
                .email("rhiannan.foreman@gmail.com")
                .password("p4ssw0rd")
                .build());

        repository.save(UserDto.builder()
                .id(UUID.randomUUID())
                .username("Darrel Neville")
                .email("darrel.neville@gmail.com")
                .password("1234567890")
                .build());

        repository.save(UserDto.builder()
                .id(UUID.randomUUID())
                .username("Audrey Chan")
                .email("a.chan@outlook.com")
                .password("welcome01")
                .build());

        repository.save(UserDto.builder()
                .id(UUID.randomUUID())
                .username("Alyssia Read")
                .email("alyssiaread@gmail.com")
                .password("98m2^LW*9%3")
                .build());

        log.debug("User Records loaded: {}", repository.count());
    }
}
