package com.ordina.authenticationservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Slf4j
public class PasswordHasher {

    private PasswordHasher() {}

    static final int STRENGTH = 12;
    static final BCryptPasswordEncoder bCryptPasswordEncoder =
            new BCryptPasswordEncoder(STRENGTH, new SecureRandom());

    public static String hash(String password) {
        String hashed = bCryptPasswordEncoder.encode(password);
        log.info("Hashing password: " + password);
        log.info("Password becomes: " + hashed);
        return hashed;
    }

    public static boolean areEqual(String plainPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, hashedPassword);
    }
}
