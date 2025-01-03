package com.mth.eshop.util;

import static com.mth.eshop.util.SecurityUtil.hasRole;

import com.mth.eshop.exception.CouponException;
import org.springframework.http.HttpStatus;

public class GlobalHelper {

  public static void validateAccessForAdmin() {
    if (!hasRole("ROLE_ADMIN")) {
      throw new CouponException(
          "Access denied: You must be an admin to perform this action.", HttpStatus.FORBIDDEN);
    }
  }

  public static void validateAccess() {
    if (!hasRole("ROLE_ADMIN", "ROLE_USER")) {
      throw new CouponException(
          "Access denied: You must be an admin or user to perform this action.",
          HttpStatus.FORBIDDEN);
    }
  }
}
