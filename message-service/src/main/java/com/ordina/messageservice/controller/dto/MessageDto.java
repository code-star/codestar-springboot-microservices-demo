package com.ordina.messageservice.controller.dto;

import com.ordina.messageservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageDto {

    private final UUID uuid;
    private final Long userId;
    private final String content;

    public MessageDto(Message message) {
        this.uuid = message.getId();
        this.userId = message.getUserId();
        this.content = message.getContent();
    }

    public MessageDto(MessageRequest messageRequest, Long userId) {
        this.uuid = UUID.randomUUID();
        this.userId = userId;
        this.content = messageRequest.content();
    }

    public Message toMessageEntity() {
        return Message.builder()
                .id(this.uuid)
                .userId(this.userId)
                .content(this.content)
                .build();
    }

    public MessageResponse toMessageResponse() {
        return new MessageResponse(this.content, this.userId);
    }
}
