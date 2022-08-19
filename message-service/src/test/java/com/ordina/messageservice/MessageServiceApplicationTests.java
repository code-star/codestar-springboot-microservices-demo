package com.ordina.messageservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordina.jwtauthlib.Jwt;
import com.ordina.jwtauthlib.client.ClientProperties;
import com.ordina.jwtauthlib.common.JwtUtils;
import com.ordina.jwtauthlib.common.PubKeyResponse;
import com.ordina.jwtauthlib.common.tokenizer.JwtToken;
import com.ordina.messageservice.controller.dto.MessageDto;
import com.ordina.messageservice.model.MessageDtoRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.security.KeyPair;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MessageServiceApplicationTests {

    public MockWebServer mockBackEnd;

    static final KeyPair keyPair = JwtUtils.generateKeyPair();
    static final String URL_BASE = "/api/v1/messages";

    static JwtToken tokenValidUser;
    static JwtToken tokenExpiredUser;
    static JwtToken tokenInvalidUser;

    static UUID validMessageId;

    @Autowired
    ClientProperties config;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageDtoRepository messageRepository;

    @BeforeAll
    void createAllTokens() {
        log.info("Creating all token...");

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

    @BeforeAll
    void startMockWebServer() throws Exception {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        config.setPublicKeyUrl(mockBackEnd.url("/").toString());

        PubKeyResponse response = new PubKeyResponse(keyPair.getPublic().getEncoded());
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(new ObjectMapper().writeValueAsString(response)));
    }

    @AfterAll
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
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
        MessageDto returnedMessage;
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        @BeforeAll
        void insertMessage() {
            message = MessageDto.builder()
                    .id(messageId)
                    .userId(userId)
                    .content("message content")
                    .build();
            returnedMessage = messageRepository.save(message);
        }

        @Test
        void messageCount_ShouldBeLargerThanZero() {
            assertThat(messageRepository.count()).isPositive();
        }

        @Test
        void findByUserId_ShouldEqualMessage() {
            assertThat(messageRepository.findAllByUserId(message.getUserId())).contains(returnedMessage);
        }

        @Test
        void findByMessageId_ShouldEqualMessage() {
            assertThat(messageRepository.findById(message.getId())).get().isEqualTo(returnedMessage);
        }

        @Test
        void insertedMessage_ShouldBeCreatedBeforeNow() {
            assertThat(returnedMessage.getCreatedAt()).isBefore(Instant.now());
            assertThat(returnedMessage.getCreatedAt()).isAfter(Instant.now().minusSeconds(10));
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
        void withValidUserId_ShouldReturnCreated() throws Exception {
            System.out.println("Bearer " + tokenValidUser.token());
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenValidUser.token())
                            .content(createMessageJSONString("Dit is een test berichtje van een valid user.")))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void withExpiredToken_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenExpiredUser.token())
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
