package com.ordina.authenticationservice.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public abstract class PasswordHasher {

    private PasswordHasher() {}

    static final int STRENGTH = 12;
    static final BCryptPasswordEncoder bCryptPasswordEncoder =
            new BCryptPasswordEncoder(STRENGTH, new SecureRandom());

    public static String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean areEqual(String plainPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(plainPassword, hashedPassword);
    }
}
