package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.ItemsDTO;
import com.mth.eshop.model.Item;

public class ItemsMapper {
  public static ItemsDTO toItemsDTO(Item item) {
    Integer numberOfReviews = item.getReview().size();

    Double roundedStars = item.getStars() != null ? Math.round(item.getStars() * 10.0) / 10.0 : 0.0;

    return new ItemsDTO(
        item.getId(),
        item.getName(),
        item.getPrice(),
        item.getDescription(),
        item.getStockQuantity(),
        roundedStars,
        numberOfReviews);
  }
}
