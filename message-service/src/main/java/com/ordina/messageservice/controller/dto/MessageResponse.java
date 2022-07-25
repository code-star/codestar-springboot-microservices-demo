package com.ordina.messageservice.controller.dto;

public record MessageResponse(
        String content,
        Long userId
) {}
