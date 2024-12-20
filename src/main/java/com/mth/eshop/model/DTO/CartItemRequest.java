package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
    @NotNull(message = "Customer ID cannot be null") Integer customerId,
    @NotBlank(message = "Item ID cannot be null") String itemId) {}
