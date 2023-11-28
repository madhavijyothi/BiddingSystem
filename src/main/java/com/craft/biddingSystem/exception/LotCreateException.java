package com.craft.biddingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LotCreateException extends RuntimeException {
    public LotCreateException(String message) {
        super(message);
    }
}
