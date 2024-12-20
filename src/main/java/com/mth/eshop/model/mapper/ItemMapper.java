package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.ItemDTO;
import com.mth.eshop.model.DTO.ReviewDTO;
import com.mth.eshop.model.Item;
import java.util.List;

public class ItemMapper {
  public static ItemDTO toItemDTO(Item item) {
    List<ReviewDTO> reviews = item.getReview().stream().map(ReviewMapper::toReviewDTO).toList();

    Double roundedStars = Math.round(item.getStars() * 10.0) / 10.0;

    return new ItemDTO(
        item.getId(),
        item.getName(),
        item.getPrice(),
        item.getDescription(),
        item.getStockQuantity(),
        roundedStars,
        reviews);
  }
}
