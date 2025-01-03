package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
    @NotBlank(message = "Customer Email cannot be blank") String email,
    @NotBlank(message = "Customer Password cannot be blank") String password,
    String firstName,
    String lastName,
    String phone) {}
