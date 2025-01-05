package com.mth.eshop.util;

import com.mth.eshop.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

public class SecurityUtil {

  /**
   * Utility to fetch the email of the currently authenticated user.
   *
   * @return the email of the current user
   * @throws IllegalStateException if no authenticated user is present
   */
  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UserException("User not logged in", HttpStatus.UNAUTHORIZED);
    }
    return authentication.getName();
  }

  /**
   * Checks if the current authenticated user has the specified role.
   *
   * @param roles the role to check (e.g., "ROLE_ADMIN")
   * @return true if the user has the role, false otherwise
   */
  public static boolean hasRole(String... roles) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authority -> Arrays.asList(roles).contains(authority));
  }
}
