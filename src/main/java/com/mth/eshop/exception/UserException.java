package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class UserException extends EshopException {
  public UserException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
