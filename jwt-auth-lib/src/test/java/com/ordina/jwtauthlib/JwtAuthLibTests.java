package com.ordina.jwtauthlib;

import com.ordina.jwtauthlib.common.JwtUtils;
import com.ordina.jwtauthlib.common.tokenizer.JwtDecodeResult;
import com.ordina.jwtauthlib.common.tokenizer.JwtToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {Jwt.class})
class JwtAuthLibTests {

    @Test
    void generateKeyPair() {
        KeyPair keypair = JwtUtils.generateKeyPair();

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
        KeyPair keypair = JwtUtils.generateKeyPair();

        UUID uuid = UUID.randomUUID();
        JwtToken token = Jwt.generator()
                .withKey(keypair.getPrivate())
                .withUserId(uuid)
                .withExpiration(JwtUtils.dateFromNowInMinutes(10));

        assertThat(token.token()).hasSize(476);

        JwtDecodeResult result = Jwt.decoder()
                .withToken(token)
                .withKey(keypair.getPublic().getEncoded());

        System.out.println(result.getErrorMessage());

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
