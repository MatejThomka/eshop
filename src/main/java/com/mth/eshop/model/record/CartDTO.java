package com.mth.eshop.model.record;

import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public record CartDTO(
    Integer id,
    Integer customerId,
    List<CartItemDTO> items,
    Integer quantity,
    Double finalPrice,
    Integer discountInPercentage) {}
