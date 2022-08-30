package com.ordina.jwtauthlib.client;

import com.ordina.jwtauthlib.common.JwtUtils;
import com.ordina.jwtauthlib.common.PubKeyResponse;
import com.ordina.jwtauthlib.exception.EmptyKeyResponseException;
import com.ordina.jwtauthlib.exception.PubKeyResponseParserException;
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
        this.config = config;
    }

    public PublicKey retrievePublicKey() {
        PubKeyResponse response = getPubKeyResponse();

        if (response == null) {
            throw new EmptyKeyResponseException();
        }

        return JwtUtils.publicKeyFromBytes(response.key());
    }

    private PubKeyResponse getPubKeyResponse() {
        return getWebClient().get()
                .retrieve()
                .bodyToMono(PubKeyResponse.class)
                .doOnError(err -> { throw new PubKeyResponseParserException(err); })
                .block();
    }

    private WebClient getWebClient() {
        log.info("Retrieving public key from: " + config.getPublicKeyUrl());
        return WebClient.builder()
                .baseUrl(config.getPublicKeyUrl())
                .build();
    }
}
