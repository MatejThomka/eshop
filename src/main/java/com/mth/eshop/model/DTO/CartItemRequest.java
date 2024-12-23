package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CartItemRequest(
    @NotNull(message = "Customer ID cannot be null") Integer customerId,
    @NotBlank(message = "Item ID cannot be null")
        @Size(min = 1, message = "Item ID cannot be empty")
        String itemId) {}
