package com.mth.eshop.model.DTO;

import java.util.List;

public record CartDTO(
    Integer id,
    Integer customerId,
    List<CartItemDTO> items,
    Integer quantity,
    Double finalPrice,
    Integer discountInPercentage) {}
