package com.ordina.messageservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final JwtKeyProvider jwtKeyProvider;

    boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getPublicKey())
                    .build()
                    .parseClaimsJws(jwt);
            return true;
        } catch(JwtException e) {
            log.warn("Invalid JWT!", e);
        }
        return false;
    }

    public Long getUserIdFrom(String jwt) {
        return getClaims(jwt).get("user_id", Long.class);
    }

    private Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKeyProvider.getPublicKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
