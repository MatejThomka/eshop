package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class ReviewException extends EshopException{
    public ReviewException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
