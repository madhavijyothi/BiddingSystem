package com.craft.biddingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductDeleteException extends RuntimeException {
    public ProductDeleteException(String message) {
        super(message);
    }
}
