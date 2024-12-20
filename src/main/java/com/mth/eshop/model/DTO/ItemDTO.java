package com.mth.eshop.model.DTO;

import java.util.List;

public record ItemDTO(
    String id,
    String name,
    Double price,
    String description,
    Integer stockQuantity,
    Double stars,
    List<ReviewDTO> reviews) {}
