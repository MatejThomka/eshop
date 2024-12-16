package com.mth.eshop.exception;

import org.springframework.http.HttpStatus;

public class CouponException extends EshopException {
  public CouponException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
