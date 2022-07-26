package com.ordina.messageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@ConfigurationProperties("ordina.message-service")
public class ServiceConfiguration {

    private String authServiceUrl;
}
