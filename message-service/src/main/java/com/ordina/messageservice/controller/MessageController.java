package com.ordina.messageservice.controller;

import com.ordina.messageservice.message.Message;
import com.ordina.messageservice.message.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    public Message uploadMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @GetMapping("/{userId}")
    public List<Message> getMessagesByUser(@PathVariable("userId") Long userId) {
        return messageRepository.findAllByUserId(userId);
    }
}
