package com.ordina.authenticationservice.security;

import com.ordina.jwtauthlib.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Service
public class JwtService {

    public static final int TOKEN_EXPIRATION_MINUTES = 10;

    private final KeyPair keyPair;

    public JwtService() {
        keyPair = Jwt.generateKeyPair();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

}
