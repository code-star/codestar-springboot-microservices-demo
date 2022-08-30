package com.ordina.jwtauthlib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Public key could not be parsed")
public class PubKeyResponseParserException extends RuntimeException {
    public PubKeyResponseParserException(Throwable err) {
        super(err);
    }
}
