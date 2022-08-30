package com.ordina.messageservice.controller.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record MessageResponse(
        String content,
        UUID userId,
        Timestamp createdAt
) {}
