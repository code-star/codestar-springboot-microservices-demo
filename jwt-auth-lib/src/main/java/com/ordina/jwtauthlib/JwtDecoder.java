package com.ordina.jwtauthlib;

import java.security.PublicKey;

public class JwtDecoder {

    public interface NeedToken {
        NeedPrivateKey withToken(String token);
    }

    public interface NeedPrivateKey {
        JwtDecodeResult withKey(PublicKey key);

        // Utility for converting a byte array into PublicKey instance
        default JwtDecodeResult withKey(byte[] key) {
            return withKey(Jwt.publicKeyFromBytes(key));
        }
    }

    public NeedToken builder() {
        return token
                -> key
                -> new JwtDecodeResult(key, token);
    }

}
