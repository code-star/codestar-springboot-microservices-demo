package com.ordina.messageservice;

import com.ordina.jwtauthlib.Jwt;
import com.ordina.messageservice.controller.dto.MessageDto;
import com.ordina.messageservice.model.MessageDtoRepository;
import com.ordina.messageservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.security.KeyPair;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class MessageServiceApplicationTests {

    static final KeyPair keyPair = Jwt.generateKeyPair();
    static final String URL_BASE = "/api/v1/messages";

    static String tokenValidUser;
    static String tokenExpiredUser;
    static String tokenInvalidUser;

    static UUID validMessageId;

    @MockBean
    JwtService jwtService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageDtoRepository messageRepository;

    @BeforeAll
    void createAllTokens() {
        MessageDto messageDto = messageRepository.findAll().get(0);
        UUID validId = messageDto.getUserId();
        UUID invalidId = UUID.randomUUID();
        validMessageId = messageDto.getId();

        tokenValidUser = Jwt.generator()
                .withKey(keyPair.getPrivate())
                .withUserId(validId)
                .witExpirationInMinutes(10);

        tokenExpiredUser = Jwt.generator()
                .withKey(keyPair.getPrivate())
                .withUserId(validId)
                .witExpirationInMinutes(-10);

        tokenInvalidUser = Jwt.generator()
                .withKey(keyPair.getPrivate())
                .withUserId(invalidId)
                .witExpirationInMinutes(10);
    }

    @BeforeEach
    void setup() {
        Mockito.when(jwtService.getPublicKey()).thenReturn(keyPair.getPublic());
    }

    @Nested
    class GetMessages {
        @Test
        void getAllMessages_ShouldReturnOk() throws Exception {
            mockMvc.perform(get(URL_BASE + "/all"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void getMessage_ShouldReturnOk() throws Exception {
            mockMvc.perform(get(URL_BASE + "/" + validMessageId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class insertingAndRetrieveMessage {
        MessageDto message;
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        @BeforeAll
        void insertMessage() {
            message = MessageDto.builder()
                    .id(messageId)
                    .userId(userId)
                    .content("message content")
                    .build();
            messageRepository.save(message);
        }

        @Test
        void messageCount_ShouldBeLargerThanZero() {
            assertThat(messageRepository.count()).isPositive();
        }

        @Test
        void findByUserId_ShouldEqualMessage() {
            assertThat(messageRepository.findAllByUserId(message.getUserId())).contains(message);
        }

        @Test
        void findByMessageId_ShouldEqualMessage() {
            assertThat(messageRepository.findById(message.getId())).get().isEqualTo(message);
        }

        @Test
        void isUnequalToOtherMessage() {
            assertThat(message)
                    .isNotEqualTo(MessageDto.builder()
                            .id(UUID.randomUUID())
                            .userId(message.getUserId())
                            .content(message.getContent())
                            .build())
                    .isNotEqualTo(MessageDto.builder()
                            .id(message.getId())
                            .userId(UUID.randomUUID())
                            .content(message.getContent())
                            .build())
                    .isNotEqualTo(MessageDto.builder()
                            .id(message.getId())
                            .userId(message.getUserId())
                            .content("different content")
                            .build());
        }
    }

    @Nested
    class UploadMessage {
        @Test
        void withValidUserId_ShouldReturnOk() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenValidUser)
                            .content(createMessageJSONString("Dit is een test berichtje van een valid user.")))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void withExpiredToken_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenExpiredUser)
                            .content(createMessageJSONString("Dit is een test berichtje van een expired user.")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void withOutAuthorization_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .content(createMessageJSONString("Dit is een test berichtje van een unauthorized user.")))
                    .andExpect(status().isForbidden());
        }
    }

    private static String createMessageJSONString(String content) {
        return "{\"content\": \"" + content + "\"}";
    }
}
