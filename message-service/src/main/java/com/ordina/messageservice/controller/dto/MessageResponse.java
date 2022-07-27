package com.ordina.messageservice.controller.dto;

import java.util.UUID;

public record MessageResponse(
        String content,
        UUID userId
) {}
