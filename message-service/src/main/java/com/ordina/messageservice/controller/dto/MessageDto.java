package com.ordina.messageservice.controller.dto;

import com.ordina.messageservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageDto {

    private final UUID id;
    private final UUID userId;
    private final String content;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.userId = message.getUserId();
        this.content = message.getContent();
    }

    public MessageDto(MessageRequest messageRequest, UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.content = messageRequest.content();
    }

    public Message toMessageEntity() {
        return new Message(this.id, this.userId, this.content, null);
    }

    public MessageResponse toMessageResponse() {
        return new MessageResponse(this.content, this.userId);
    }
}
