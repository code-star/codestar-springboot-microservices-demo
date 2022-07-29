package com.ordina.jwtauthlib.client;

import com.ordina.jwtauthlib.common.JwtUtils;
import com.ordina.jwtauthlib.common.PubKeyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.PublicKey;

@Slf4j
@Service
public class JwtClientService {

    private final ClientProperties config;

    private PublicKey publicKey = null;

    public PublicKey getPublicKey() {
        if (publicKey == null) {
            publicKey = this.retrievePublicKey();
        }
        return publicKey;
    }

    public JwtClientService(ClientProperties config) {
        log.info("JwtClientService instantiated");
        this.config = config;
    }

    public PublicKey retrievePublicKey() {
        PubKeyResponse response = getPubKeyResponse();

        if (response == null) {
            throw new RuntimeException("Empty response from authentication server, is it up?");
        }

        return JwtUtils.publicKeyFromBytes(response.key());
    }

    private PubKeyResponse getPubKeyResponse() {
        return getWebClient().get()
                .retrieve()
                .bodyToMono(PubKeyResponse.class)
                .doOnError(err -> { throw new RuntimeException(err); })
                .block();
    }

    private WebClient getWebClient() {
        log.info("Retrieving public key from: " + config.getPublicKeyUrl());
        return WebClient.builder()
                .baseUrl(config.getPublicKeyUrl())
                .build();
    }
}
