package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartCouponRequest(
    @NotNull(message = "Customer ID cannot be null") Integer customerId,
    @NotBlank(message = "Review ID cannot be blank") String couponId) {}
