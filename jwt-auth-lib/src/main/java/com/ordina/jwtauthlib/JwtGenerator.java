package com.ordina.jwtauthlib;

import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.util.Date;
import java.util.UUID;

public class JwtGenerator {

    public interface NeedPrivateKey {
        NeedUserId withKey(PrivateKey key);
    }

    public interface NeedUserId {
        NeedExpiration withUserId(UUID id);
    }

    public interface NeedExpiration {
        String withExpiration(Date expiration);
        default String witExpirationInMinutes(int minutes) {
            return withExpiration(Jwt.dateFromNowInMinutes(minutes));
        }
    }

    public NeedPrivateKey builder(){
        return key
            -> userId
            -> expiration
            -> Jwts.builder()
                .setExpiration(expiration)
                .signWith(key)
                .claim("user_id", userId.toString())
                .setIssuer("codestar")
                .compact();
    }
}
