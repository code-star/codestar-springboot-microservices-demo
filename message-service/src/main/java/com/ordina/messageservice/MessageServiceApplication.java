package com.ordina.messageservice;

import com.ordina.jwtauthlib.EnableAuthenticationClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableAuthenticationClient
@EnableGlobalMethodSecurity(prePostEnabled=true)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }

}
