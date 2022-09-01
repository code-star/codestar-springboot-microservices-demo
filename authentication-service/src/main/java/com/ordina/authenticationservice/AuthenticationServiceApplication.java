package com.ordina.authenticationservice;

import com.ordina.jwtauthlib.EnableAuthenticationClient;
import com.ordina.jwtauthlib.EnableAuthenticationHost;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@EnableAuthenticationClient
@EnableAuthenticationHost
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

}
