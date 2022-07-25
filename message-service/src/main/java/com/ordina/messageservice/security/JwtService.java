package com.ordina.messageservice.security;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.messageservice.config.ServiceConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.PublicKey;

@Component
@Slf4j
public class JwtService {

    private final ServiceConfiguration config;

    @Getter
    private final PublicKey publicKey;

    public JwtService(ServiceConfiguration config) {
        this.config = config;
        publicKey = this.retrievePublicKey();
    }

    public PublicKey retrievePublicKey() {
        log.info("Retrieving public key from webserver...");
        PubKeyResponse response = getPubKeyResponse();

        if (response == null) {
            throw new RuntimeException("Empty response from authentication server, is it up?");
        }

        return Jwt.publicKeyFromBytes(response.key());
    }

    private PubKeyResponse getPubKeyResponse() {
        return getWebClient().get()
                .retrieve()
                .bodyToMono(PubKeyResponse.class)
                .doOnError(err -> { throw new RuntimeException(err); })
                .block();
    }

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(config.getAuthServiceUrl())
                .build();
    }

    public record PubKeyResponse(byte[] key) { }
}
