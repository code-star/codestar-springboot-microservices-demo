package com.ordina.messageservice.controller.dto;

import com.ordina.messageservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageDto {

    private final UUID id;
    private final UUID userId;
    private final String content;
    private final Timestamp createdAt;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.userId = message.getUserId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
    }

    public MessageDto(MessageRequest messageRequest, UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.content = messageRequest.content();
        this.createdAt = null;
    }

    public Message toMessageEntity() {
        return new Message(this.id, this.userId, this.content, null);
    }

    public MessageResponse toMessageResponse() {
        return new MessageResponse(this.content, this.userId, this.createdAt);
    }
}
