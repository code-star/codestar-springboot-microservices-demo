package com.ordina.jwtauthlib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_GATEWAY, reason="Empty response from authentication server, is it up?")
public class EmptyKeyResponseException extends RuntimeException {
}
