package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class CartException extends EshopException {
  public CartException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
