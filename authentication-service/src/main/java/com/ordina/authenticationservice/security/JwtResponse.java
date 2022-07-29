package com.ordina.authenticationservice.security;

import lombok.Builder;

@Builder
public record JwtResponse(String token, String userId) {}
