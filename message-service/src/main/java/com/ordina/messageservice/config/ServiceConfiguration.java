package com.ordina.messageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Component
@ConfigurationProperties("ordina.message-service")
public class ServiceConfiguration {

    private String authServiceUrl;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
