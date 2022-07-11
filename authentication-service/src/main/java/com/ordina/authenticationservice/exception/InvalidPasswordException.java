package com.ordina.authenticationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Invalid password")
public class InvalidPasswordException extends RuntimeException {}
