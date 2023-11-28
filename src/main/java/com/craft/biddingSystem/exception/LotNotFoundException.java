package com.craft.biddingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(String message) {
        super(message);
    }
}
