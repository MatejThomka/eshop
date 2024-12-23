package com.mth.eshop.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EshopExceptionHandler {

  @ExceptionHandler(EshopException.class)
  public ResponseEntity<ErrorResponse> handleEshopException(EshopException e) {
    ErrorResponse error =
        new ErrorResponse() {
          @NonNull
          @Override
          public HttpStatusCode getStatusCode() {
            return e.getHttpStatus();
          }

          @NonNull
          @Override
          public ProblemDetail getBody() {
            return e.toProblemDetail();
          }
        };
    return ResponseEntity.status(e.getHttpStatus()).body(error);
  }
}
