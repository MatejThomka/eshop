package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class ItemException extends EshopException {
  public ItemException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
