package com.mth.eshop.model;

public enum Role {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public static Role getRole(String role) {
    for (Role r : Role.values()) {
      if (r.value.equals(role)) {
        return r;
      }
    }
    return null;
  }
}
