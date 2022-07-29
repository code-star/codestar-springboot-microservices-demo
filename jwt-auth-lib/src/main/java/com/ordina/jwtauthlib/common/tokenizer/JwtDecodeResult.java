package com.ordina.jwtauthlib.common.tokenizer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PublicKey;
import java.util.UUID;

public class JwtDecodeResult {
    private boolean valid = false;
    private Claims claims = null;
    private String errorMessage = null;

    JwtDecodeResult(PublicKey key, String token) {
        try {
            this.claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer("codestar")
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (this.claims != null) {
                this.valid = true;
            }
        } catch (JwtException | IllegalArgumentException err) {
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
        if (this.claims == null) return null;
        String uuidStr = this.claims.get("user_id", String.class);
        return UUID.fromString(uuidStr);
    }
}
