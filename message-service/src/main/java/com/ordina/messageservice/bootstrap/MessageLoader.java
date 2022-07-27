package com.ordina.messageservice.bootstrap;

import com.ordina.messageservice.controller.dto.MessageDto;
import com.ordina.messageservice.model.MessageDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageLoader implements CommandLineRunner {

    private final MessageDtoRepository messageRepository;

    @Override
    public void run(String... args) {
        loadMessageObjects();
    }

    private synchronized void loadMessageObjects() {
        log.debug("Loading initial Message objects into the database.");

        // Don't add more users if database is already populated
        if (messageRepository.count() != 0)
            return;

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        messageRepository.save(MessageDto.builder()
                .id(UUID.randomUUID())
                .userId(id1)
                .content("Hello World!")
                .build());

        messageRepository.save(MessageDto.builder()
                .id(UUID.randomUUID())
                .userId(id1)
                .content("Welcome to my channel! I can post messages that can be viewed by everyone!")
                .build());

        messageRepository.save(MessageDto.builder()
                .id(UUID.randomUUID())
                .userId(id2)
                .content("Eyoo, got this is sweeeeet!")
                .build());
    }
}
