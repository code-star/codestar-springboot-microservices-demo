package com.ordina.jwtauthlib;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {Jwt.class})
class JwtAuthLibApplicationTests {

    @Test
    @Order(1)
    void generateKeyPair() {
        KeyPair keypair = Jwt.generateKeyPair();

        assertThat(keypair).isNotNull();
        assertThat(keypair.getPrivate()).isNotNull();
        assertThat(keypair.getPublic()).isNotNull();

        assertThat(keypair.getPublic().getAlgorithm()).isEqualTo("RSA");
        assertThat(keypair.getPublic().getFormat()).isEqualTo("X.509");

        assertThat(keypair.getPrivate().getAlgorithm()).isEqualTo("RSA");
        assertThat(keypair.getPrivate().getFormat()).isEqualTo("PKCS#8");
    }

    @Test
    @Order(2)
    void generateAndValidateToken() {
        KeyPair keypair = Jwt.generateKeyPair();

        String token = Jwt.generator()
                .withKey(keypair.getPrivate())
                .withUserId(15)
                .withExpiration(Jwt.dateFromNowInMinutes(10));

        assertThat(token).hasSize(460);

        JwtDecodeResult result = Jwt.decoder()
                .withToken(token)
                .withKey(keypair.getPublic().getEncoded());

        assertThat(result.isValid()).isTrue();
        assertThat(result.getUserId()).isEqualTo(15);

        var decoderThatNeedsKey = Jwt.decoder().withToken(token);
        assertThatThrownBy(() ->
                decoderThatNeedsKey.withKey(new byte[10])) // Invalid byte key
                .isInstanceOf(RuntimeException.class);
    }
}
