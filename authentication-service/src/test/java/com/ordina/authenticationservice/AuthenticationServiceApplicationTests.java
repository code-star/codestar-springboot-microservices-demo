package com.ordina.authenticationservice;

import com.ordina.authenticationservice.controller.AuthController;
import com.ordina.authenticationservice.user.User;
import com.ordina.authenticationservice.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan({ "com.ordina.authenticationservice", "com.ordina.jwtauthlib" })
class AuthenticationServiceApplicationTests {

    @Autowired
    AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private static final String URL_BASE = "/api/v1/auth";

    @Nested
    class PreloadAndAuthenticate {
        @Test
        void isDatabasePreloaded() {
            Optional<User> rhiannan = userRepository.findByUsername("Rhiannan Foreman");

            assertThat(rhiannan).isPresent();
            assertThat(rhiannan.get().getPassword()).isEqualTo("p4ssw0rd");
            assertThat(rhiannan.get().getEmail()).isEqualTo("rhiannan.foreman@gmail.com");
            assertThat(rhiannan.get().getEnabled()).isTrue();
        }

        @Test
        void canAuthenticatePreloadedUser() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .content(getCredentialsJSONString("Audrey Chan", "welcome01")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }
    }

    @Nested
    class RegisterAndAuthenticateUser {
        @Test
        @Order(1)
        void registerUser() throws Exception {
            mockMvc.perform(post(URL_BASE + "/register").contentType(MediaType.APPLICATION_JSON)
                            .content(getUserJSONString("Foo-Bar", "foo.bar@ordina.nl", "p4ssw0rd", "admin", true)))
                    .andExpect(status().isCreated());
        }

        @Test
        @Order(2)
        void authenticateUser() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                    .content(getCredentialsJSONString("Foo-Bar", "p4ssw0rd")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }
    }

    @Nested
    class InvalidCredentials {
        @Test
        void invalidUsernameReturnsNotFoundError() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                    .content(getCredentialsJSONString("Non-existing User", "123")))
                    .andExpect(status().isNotFound());
        }

        @Test
        void invalidPasswordReturnsUnauthorized() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .content(getCredentialsJSONString("Darrel Neville", "wrong password")))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void publicKeyIsHosted() throws Exception {
        mockMvc.perform(get(URL_BASE + "/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").isNotEmpty());
    }

    private static String getUserJSONString(String username, String email, String password, String role, Boolean enabled) {
        return "{\"username\": \"" + username + "\",\"email\": \"" + email + "\",\"password\": \"" + password + "\",\"role\": \"" + role + "\",\"enabled\": " + enabled.toString() + "}";
    }

    private static String getCredentialsJSONString(String username, String password) {
        return "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
    }

}
