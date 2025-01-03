package com.mth.eshop.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
public class EshopException extends RuntimeException {

  private final HttpStatus httpStatus;

  public EshopException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public ProblemDetail toProblemDetail() {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, getMessage());
    problemDetail.setTitle("Eshop problem");
    problemDetail.setInstance(URI.create("/eshop/problem"));
    problemDetail.setProperties(properties());

    return problemDetail;
  }

  private Map<String, Object> properties() {
    Map<String, Object> properties = new HashMap<>();

    switch (httpStatus) {
      case FORBIDDEN -> {
        properties.put("errorCode", "ESHOP-403");
        properties.put("errorType", "Access denied");
        properties.put("suggestion", "Please ensure you have the correct role.");
        properties.put("currentUser", getCurrentUserInfo());
      }
      case NOT_FOUND -> {
        properties.put("errorCode", "ESHOP-404");
        properties.put("errorType", "Resource not found");
        properties.put("suggestion", "The requested resource could not be found.");
        properties.put("currentUser", getCurrentUserInfo());
      }
      case BAD_REQUEST -> {
        properties.put("errorCode", "ESHOP-400");
        properties.put("errorType", "Validation error");
        properties.put("suggestion", "Ensure the input data is valid and complete.");
        properties.put("currentUser", getCurrentUserInfo());
      }
      default -> {
        properties.put("errorCode", "ESHOP-500");
        properties.put("errorType", "Internal server error");
        properties.put("suggestion", "Please contact the system administrator.");
        properties.put("currentUser", getCurrentUserInfo());
      }
    }

    return properties;
  }

  private String getCurrentUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User user) {
      return String.format("User: %s, Role: %s", user.getUsername(), user.getAuthorities());
    }
    return "Anonymous";
  }
}
