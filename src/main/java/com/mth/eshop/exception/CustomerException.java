package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class CustomerException extends EshopException {
  public CustomerException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
