package com.ordina.authenticationservice.controller.dto;

public record UserRequest(
    String username,
    String password,
    String email
) {}
