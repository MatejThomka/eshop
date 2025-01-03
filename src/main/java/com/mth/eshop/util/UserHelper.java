package com.mth.eshop.util;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.UserException;
import org.springframework.http.HttpStatus;

public class UserHelper {

  public static void isEmailValid(String email) {
    if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$"))
      throw new UserException("Email is not valid", HttpStatus.BAD_REQUEST);
  }

  public static void isPasswordValid(String password) {
    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"))
      throw new UserException(
          "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace!",
          HttpStatus.BAD_REQUEST);
  }

  public static void isPhoneValid(String phone) {
    if (!phone.matches("^\\d{10}$"))
      throw new UserException("Phone number must be 10 digits!", HttpStatus.BAD_REQUEST);
  }

  public static void isIdValid(int id) throws EshopException {
    if (id < 0)
      throw new UserException("Customer ID cannot be negative!", HttpStatus.BAD_REQUEST);
  }

  public static void isPasswordMatch(String password, String confirmPassword) {
    if (!password.equals(confirmPassword))
      throw new UserException("Incorrect credentials!", HttpStatus.BAD_REQUEST);
  }
}
