package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Item;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.model.record.ReviewDTO;
import java.util.List;

public class ItemMapper {
  public static ItemDTO toItemDTO(Item item) {
    List<ReviewDTO> reviewDTOS = item.getReview().stream().map(ReviewMapper::toReviewDTO).toList();

    return new ItemDTO(
        item.getId(),
        item.getName(),
        item.getPrice(),
        item.getDescription(),
        item.getStockQuantity(),
        reviewDTOS);
  }
}
