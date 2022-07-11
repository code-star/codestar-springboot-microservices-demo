package com.ordina.authenticationservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private static final long TOKEN_EXPIRATION_MINUTES = 10;

    @Getter
    @Autowired
    private JwtKeyProvider keyProvider;

    public String generateToken(Long userId) {
        LocalDateTime expiryDate = LocalDateTime
                .now()
                .plusMinutes(TOKEN_EXPIRATION_MINUTES);

        return Jwts.builder()
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(keyProvider.getKeyPair().getPrivate(), SignatureAlgorithm.RS256)
                .claim("user_id", userId)
                .compact();
    }
}
