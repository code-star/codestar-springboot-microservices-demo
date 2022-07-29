package com.ordina.jwtauthlib.common;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public interface JwtUtils {
    static Date dateFromNowInMinutes(int minutesFromNow) {
        return Date.from(Instant.now().plus(minutesFromNow, ChronoUnit.MINUTES));
    }

    static KeyPair generateKeyPair() {
        return Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    static PublicKey publicKeyFromBytes(byte[] pubKey) {
        try {
            X509EncodedKeySpec ks = new X509EncodedKeySpec(pubKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(ks);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException err) {
            throw new RuntimeException(err.getLocalizedMessage(), err.getCause());
        }
    }
}
