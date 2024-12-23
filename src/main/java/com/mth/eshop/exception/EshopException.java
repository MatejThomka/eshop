package com.mth.eshop.exception;

import java.net.URI;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
public class EshopException extends RuntimeException {

  private final HttpStatus httpStatus;

  public EshopException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public ProblemDetail toProblemDetail() {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, getMessage());
    problemDetail.setTitle("Eshop error");
    problemDetail.setInstance(URI.create("/eshop/problem"));
    return problemDetail;
  }
}
