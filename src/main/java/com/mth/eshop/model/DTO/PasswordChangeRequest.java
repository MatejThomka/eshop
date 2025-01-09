package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
    @NotBlank(message = "Old password can not be blank.") String oldPassword,
    @NotBlank(message = "New password can not be blank.") String newPassword,
    @NotBlank(message = "Confirm password can not be blank.") String confirmPassword) {}
