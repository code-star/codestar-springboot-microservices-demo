package com.ordina.jwtauthlib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Username not present in JWT token")
public class UsernameNotPresentException extends RuntimeException {
}
