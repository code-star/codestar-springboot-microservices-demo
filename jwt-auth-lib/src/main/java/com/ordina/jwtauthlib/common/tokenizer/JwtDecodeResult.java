package com.ordina.jwtauthlib.common.tokenizer;

import com.ordina.jwtauthlib.exception.UsernameNotPresentException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PublicKey;
import java.util.UUID;

public class JwtDecodeResult {
    private boolean valid = false;
    private Claims claims = null;
    private String errorMessage = null;

    JwtDecodeResult(PublicKey key, JwtToken token) {
        try {
            this.claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer("codestar")
                    .build()
                    .parseClaimsJws(token.token())
                    .getBody();
            this.valid = this.claims != null;
        } catch (JwtException | IllegalArgumentException | NullPointerException err) {
            this.errorMessage = err.getLocalizedMessage();
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public UUID getUserId() {
        if (!isValid()) return null;
        String uuidStr = this.claims.get("user_id", String.class);
        if (uuidStr == null) {
            throw new UsernameNotPresentException();
        }
        return UUID.fromString(uuidStr);
    }
}
