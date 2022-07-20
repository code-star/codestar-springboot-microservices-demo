package com.ordina.messageservice.bootstrap;

import com.ordina.messageservice.message.Message;
import com.ordina.messageservice.message.MessageDto;
import com.ordina.messageservice.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageLoader implements CommandLineRunner {

    private final MessageRepository messageRepository;

    @Override
    public void run(String... args) {
        loadMessageObjects();
    }

    private synchronized void loadMessageObjects() {
        log.debug("Loading initial Message objects into the database.");

        // Don't add more users if database is already populated
        if (messageRepository.count() != 0)
            return;

        messageRepository.save(new Message(MessageDto.builder()
                .userId(1L)
                .content("Hello World!")
                .build()));

        messageRepository.save(new Message(MessageDto.builder()
                .userId(1L)
                .content("Welcome to my channel! I can post messages that can be viewed by everyone!")
                .build()));

        messageRepository.save(new Message(MessageDto.builder()
                .userId(2L)
                .content("Eyoo, got this is sweeeeet!")
                .build()));
    }
}
