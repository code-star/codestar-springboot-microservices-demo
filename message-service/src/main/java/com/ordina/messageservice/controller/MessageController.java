package com.ordina.messageservice.controller;

import com.ordina.jwtauthlib.MyUserDetails;
import com.ordina.messageservice.controller.dto.MessageRequest;
import com.ordina.messageservice.controller.dto.MessageResponse;
import com.ordina.messageservice.controller.dto.MessageDto;
import com.ordina.messageservice.model.MessageDtoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageDtoRepository messageRepository;

    public MessageController(MessageDtoRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping
    @PreAuthorize("authentication.principal.id != null")
    public MessageResponse uploadMessage(@RequestBody MessageRequest messageRequest) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MessageDto messageDto = messageRepository.save(messageRequest, userDetails.getId());
        return messageDto.toMessageResponse();
    }

    @GetMapping("/{userId}")
    public List<MessageResponse> getMessagesByUser(@PathVariable("userId") UUID userId) {
        return messageRepository.findAllByUserId(userId).stream()
                .map(MessageDto::toMessageResponse)
                .toList();
    }

    @GetMapping("/all")
    public List<MessageResponse> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(MessageDto::toMessageResponse)
                .toList();
    }
}
