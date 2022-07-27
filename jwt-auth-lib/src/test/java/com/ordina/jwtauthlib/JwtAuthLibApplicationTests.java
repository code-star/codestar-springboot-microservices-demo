package com.ordina.jwtauthlib;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {Jwt.class})
class JwtAuthLibApplicationTests {

    @Test
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
    void generateAndValidateToken() {
        KeyPair keypair = Jwt.generateKeyPair();

        UUID uuid = UUID.randomUUID();
        String token = Jwt.generator()
                .withKey(keypair.getPrivate())
                .withUserId(uuid)
                .withExpiration(Jwt.dateFromNowInMinutes(10));

        assertThat(token).hasSize(508);

        JwtDecodeResult result = Jwt.decoder()
                .withToken(token)
                .withKey(keypair.getPublic().getEncoded());

        assertThat(result.isValid()).isTrue();
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getUserId()).isInstanceOf(UUID.class);
        assertThat(result.getUserId()).isEqualTo(uuid);

        var decoderThatNeedsKey = Jwt.decoder().withToken(token);
        assertThatThrownBy(() ->
                decoderThatNeedsKey.withKey(new byte[10])) // Invalid byte key
                .isInstanceOf(RuntimeException.class);
    }
}
