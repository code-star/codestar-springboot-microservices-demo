package com.ordina.messageservice.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageDto {

    public MessageDto(Message message) {
        this.userId = message.getUserId();
        this.content = message.getContent();
    }

    private Long userId;
    private String content;
}
