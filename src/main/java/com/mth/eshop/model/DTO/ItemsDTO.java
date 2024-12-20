package com.mth.eshop.model.DTO;

public record ItemsDTO(
    String id,
    String name,
    Double price,
    String description,
    Integer stockQuantity,
    Double stars,
    Integer numberOfReviews) {}
