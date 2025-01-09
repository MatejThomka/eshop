package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;

public record EmailChangeRequest(
    @NotBlank(message = "Old email can not be empty or blank.") String oldEmail,
    @NotBlank(message = "New email can not be empty or blank.") String newEmail,
    @NotBlank(message = "Confirm email can not be empty or blank.") String confirmEmail) {}
