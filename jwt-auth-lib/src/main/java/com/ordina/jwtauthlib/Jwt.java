package com.ordina.jwtauthlib;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public interface Jwt {

    static JwtDecoder.NeedToken decoder() {
        return new JwtDecoder().builder();
    }

    static JwtGenerator.NeedPrivateKey generator() {
        return new JwtGenerator().builder();
    }

    static Date dateFromNowInMinutes(int minutesFromNow) {
        return Date.from(LocalDateTime.now().plusMinutes(minutesFromNow).atZone(ZoneId.systemDefault()).toInstant());
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
