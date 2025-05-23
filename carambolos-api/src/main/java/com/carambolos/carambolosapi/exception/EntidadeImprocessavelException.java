package com.carambolos.carambolosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntidadeImprocessavelException extends RuntimeException {
    public EntidadeImprocessavelException(String message) {
        super(message);
    }
}
