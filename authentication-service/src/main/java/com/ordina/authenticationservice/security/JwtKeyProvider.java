package com.ordina.authenticationservice.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyPair;

@Component
public class JwtKeyProvider {

    @Getter
    private KeyPair keyPair;

    @PostConstruct
    private void init() {
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    }
}
