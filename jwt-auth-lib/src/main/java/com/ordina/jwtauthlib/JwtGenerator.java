package com.ordina.jwtauthlib;

import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.util.Date;

public class JwtGenerator {

    public interface NeedPrivateKey {
        NeedUserId withKey(PrivateKey key);
    }

    public interface NeedUserId {
        NeedExpiration withUserId(Long id);
        default NeedExpiration withUserId(Integer id) {
            return withUserId(Long.valueOf(id));
        }
    }

    public interface NeedExpiration {
        String withExpiration(Date expiration);
        default String witExpiration(int minutes) {
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
                .claim("user_id", userId)
                .setIssuer("codestar")
                .claim("validation", "codestar")
                .compact();
    }

}
