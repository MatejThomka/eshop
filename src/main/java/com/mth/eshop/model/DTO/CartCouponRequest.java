package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CartCouponRequest(
    @NotNull(message = "Customer ID cannot be null") Integer customerId,
    @NotBlank(message = "Review ID cannot be blank")
        @Size(min = 1, message = "Item ID cannot be empty")
        String couponId) {}
