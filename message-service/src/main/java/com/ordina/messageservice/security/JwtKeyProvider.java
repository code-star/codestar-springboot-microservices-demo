package com.ordina.messageservice.security;

import com.ordina.messageservice.config.ServiceConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.internal.ServiceDependencyException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtKeyProvider {

    private final ServiceConfiguration config;

    @Getter
    private PublicKey publicKey;

    private final RestTemplate restTemplate;

    @PostConstruct
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        publicKey = retrievePublicKey();
    }

    private PublicKey retrievePublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PubKeyResponse response = restTemplate.getForObject(config.getAuthServiceUrl(), PubKeyResponse.class);

        if (response == null) {
            throw new ServiceDependencyException("Public key not published correctly.");
        }

        X509EncodedKeySpec ks = new X509EncodedKeySpec(response.key());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(ks);
    }

    public record PubKeyResponse(byte[] key) {}
}
