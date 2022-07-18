package com.ordina.messageservice.security;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.messageservice.config.ServiceConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Component
@Slf4j
public class JwtService {

    @Autowired
    private ServiceConfiguration config;

    @Getter
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = this.retrievePublicKey();
    }

    public PublicKey retrievePublicKey() {
        log.info("Retrieving public key from webserver...");
        WebClient webClient = getWebClient();
        PubKeyResponse response = webClient.get()
                .retrieve()
                .bodyToMono(PubKeyResponse.class)
                .doOnError(err -> { throw new RuntimeException(err); })
                .block();

        if (response == null) {
            throw new RuntimeException("Empty response");
        }

        return Jwt.publicKeyFromBytes(response.key());
    }

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(config.getAuthServiceUrl())
                .build();
    }

    public record PubKeyResponse(byte[] key) { }
}
