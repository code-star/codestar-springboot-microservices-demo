package com.ordina.messageservice.controller;

import com.ordina.messageservice.message.Message;
import com.ordina.messageservice.message.MessageDto;
import com.ordina.messageservice.message.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    @PreAuthorize("#messageDto.userId == authentication.principal.id")
    public MessageDto uploadMessage(@RequestBody MessageDto messageDto) {
        messageRepository.save(new Message(messageDto));
        return messageDto;
    }

    @GetMapping("/{userId}")
    public List<MessageDto> getMessagesByUser(@PathVariable("userId") Long userId) {
        return messageRepository.findAllDtoByUserId(userId);
    }
}
