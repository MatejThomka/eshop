package com.mth.eshop.model.record;

import java.util.List;

public record ItemDTO(
    String id,
    String name,
    Double price,
    String description,
    Integer stockQuantity,
    List<ReviewDTO> reviewDTOS) {}
