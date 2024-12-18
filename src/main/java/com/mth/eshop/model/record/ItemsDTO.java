package com.mth.eshop.model.record;

public record ItemsDTO(
    String id,
    String name,
    Double price,
    String description,
    Integer stockQuantity,
    Double stars,
    Integer numberOfReviews) {}
