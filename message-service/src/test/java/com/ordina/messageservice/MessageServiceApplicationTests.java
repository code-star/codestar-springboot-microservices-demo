package com.ordina.messageservice;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.messageservice.message.Message;
import com.ordina.messageservice.message.MessageRepository;
import com.ordina.messageservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ComponentScan(value = { "com.ordina.messageservice", "com.ordina.jwtauthlib" }, lazyInit = true)
class MessageServiceApplicationTests {

    static final KeyPair keyPair = Jwt.generateKeyPair();
    private static final String URL_BASE = "/api/v1/messages";
    static final String token_valid_user_1 = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(1).witExpiration(10);
    static final String token_expired_user_1 = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(1).witExpiration(-10);
    static final String token_invalid_user = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(-1).witExpiration(10);

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setup() {
        Mockito.when(jwtService.getPublicKey()).thenReturn(keyPair.getPublic());
    }

    @Test
    void GetMessages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(URL_BASE + "/1")
                                .header("authorization", "Bearer " + token_valid_user_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Nested
    @Order(1)
    class InsertMessageAndRetrieve {
        @Test
        @Order(1)
        void isDatabasePreloaded() {
            long count = messageRepository.count();

            Message message = Message.builder()
                    .userId(1L)
                    .content("message content")
                    .build();
            messageRepository.save(message);

            assertThat(count).isEqualTo(messageRepository.count() - 1);

            assertThat(messageRepository.findById(message.getId())).isPresent();
            assertThat(messageRepository.findById(message.getId()).get()).isEqualTo(message);
        }
    }

    @Nested
    @Order(2)
    class UploadMessage {
        @Test
        void withValidUserId_ShouldReturnOk() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_valid_user_1)
                            .content(getMessageJSONString(1, "Dit is een test berichtje van een valid user.")))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void withInvalidUserId_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_valid_user_1)
                            .content(getMessageJSONString(10, "Dit is een test berichtje van een valid user gepost onder verkeerde user.")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void withExpiredToken_ShouldReturnUnauthorized() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_expired_user_1)
                            .content(getMessageJSONString(1, "Dit is een test berichtje van een expired user.")))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void withInvalidUser_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_invalid_user)
                            .content(getMessageJSONString(1, "Dit is een test berichtje van een invalid user.")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void withOutAuthorization_ShouldReturnUnauthorized() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .content(getMessageJSONString(1, "Dit is een test berichtje van een unauthorized user.")))
                    .andExpect(status().isUnauthorized());
        }
    }

    private static String getMessageJSONString(int userId, String content) {
        return "{\"userId\": \"" + userId + "\",\"content\": \"" + content + "\"}";
    }
}
