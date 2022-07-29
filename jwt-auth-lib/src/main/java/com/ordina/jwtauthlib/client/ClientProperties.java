package com.ordina.jwtauthlib.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@ConfigurationProperties("jwt-auth-lib")
public class ClientProperties {
    private String publicKeyUrl;
}
