package com.carambolos.carambolosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeJaExisteException extends RuntimeException {
    public EntidadeJaExisteException(String message) {
        super(message);
    }
}
