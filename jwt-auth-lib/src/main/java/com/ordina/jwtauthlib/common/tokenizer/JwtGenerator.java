package com.ordina.jwtauthlib.common.tokenizer;

import com.ordina.jwtauthlib.common.JwtUtils;
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
        JwtToken withExpiration(Date expiration);
        default JwtToken witExpirationInMinutes(int minutes) {
            return withExpiration(JwtUtils.dateFromNowInMinutes(minutes));
        }
    }

    public NeedPrivateKey builder(){
        return key
            -> userId
            -> expiration
            -> new JwtToken(Jwts.builder()
                .setExpiration(expiration)
                .signWith(key)
                .claim("user_id", userId.toString())
                .setIssuer("codestar")
                .compact());
    }
}
