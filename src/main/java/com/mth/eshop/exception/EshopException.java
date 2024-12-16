package com.mth.eshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EshopException extends RuntimeException {

  private String message;

  private HttpStatus httpStatus;

  public EshopException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public EshopException(String message) {
    this.message = message;
  }

  public EshopException(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
