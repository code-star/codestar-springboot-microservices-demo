package com.ordina.jwtauthlib.common.tokenizer;

import com.ordina.jwtauthlib.common.JwtUtils;

import java.security.PublicKey;

public class JwtDecoder {

    public interface NeedToken {
        NeedPrivateKey withToken(JwtToken token);
    }

    public interface NeedPrivateKey {
        JwtDecodeResult withKey(PublicKey key);

        // Utility for converting a byte array into PublicKey instance
        default JwtDecodeResult withKey(byte[] key) {
            return withKey(JwtUtils.publicKeyFromBytes(key));
        }
    }

    public NeedToken builder() {
        return token
                -> key
                -> new JwtDecodeResult(key, token);
    }

}
