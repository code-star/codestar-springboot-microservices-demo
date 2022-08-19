package com.ordina.jwtauthlib.host;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.jwtauthlib.common.JwtUtils;
import com.ordina.jwtauthlib.common.tokenizer.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

@Slf4j
@Service
public class JwtHostService {

    public static final int TOKEN_EXPIRATION_MINUTES = 10;
    private final KeyPair keyPair = JwtUtils.generateKeyPair();

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public JwtToken getAccessTokenForUser(UUID userId) {
        return Jwt.generator()
                .withKey(getPrivateKey())
                .withUserId(userId)
                .witExpirationInMinutes(TOKEN_EXPIRATION_MINUTES);
    }
}
