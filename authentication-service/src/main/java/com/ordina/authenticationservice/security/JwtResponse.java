package com.ordina.authenticationservice.security;

import com.ordina.jwtauthlib.common.tokenizer.JwtToken;
import lombok.Builder;

@Builder
public record JwtResponse(String token, String userId) {}
