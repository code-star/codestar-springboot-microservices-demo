package com.ordina.messageservice.controller.dto;

import com.ordina.messageservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MessageDto {

    private final UUID id;
    private final Long userId;
    private final String content;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.userId = message.getUserId();
        this.content = message.getContent();
    }

    public MessageDto(MessageRequest messageRequest, Long userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.content = messageRequest.content();
    }

    public Message toMessageEntity() {
        return Message.builder()
                .id(this.id)
                .userId(this.userId)
                .content(this.content)
                .build();
    }

    public MessageResponse toMessageResponse() {
        return new MessageResponse(this.content, this.userId);
    }
}
