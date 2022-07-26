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
    private static final String URL_BASE = "/api/v1/messages";
    static final String token_valid_user_1 = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(1).witExpiration(10);
    static final String token_expired_user_1 = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(1).witExpiration(-10);
    static final String token_invalid_user = Jwt.generator().withKey(keyPair.getPrivate()).withUserId(-1).witExpiration(10);

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageDtoRepository messageRepository;

    @BeforeEach
    void setup() {
        Mockito.when(jwtService.getPublicKey()).thenReturn(keyPair.getPublic());
    }

    @Test
    void getMessages() throws Exception {
        mockMvc.perform(get(URL_BASE + "/1")
                        .header("authorization", "Bearer " + token_valid_user_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void isDatabasePreloaded() {
        long count = messageRepository.count();
        MessageDto message = MessageDto.builder()
                .userId(1L)
                .content("message content")
                .build();
        message = messageRepository.save(message);

        assertThat(count).isEqualTo(messageRepository.count() - 1);
        assertThat(messageRepository.findAllByUserId(message.getUserId())).contains(message);
        assertThat(messageRepository.findById(message.getUuid())).get().isEqualTo(message);
    }

    @Nested
    class UploadMessage {
        @Test
        void withValidUserId_ShouldReturnOk() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_valid_user_1)
                            .content(createMessageJSONString("Dit is een test berichtje van een valid user.")))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void withExpiredToken_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + token_expired_user_1)
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
