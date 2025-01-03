package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank(message = "Customer Email cannot be blank") String email,
    @NotBlank(message = "Customer Password cannot be blank") String password,
    boolean stayLoggedIn) {}
