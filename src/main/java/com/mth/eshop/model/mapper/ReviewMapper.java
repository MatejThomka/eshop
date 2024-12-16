package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Review;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.model.record.ReviewDTO;

public class ReviewMapper {
  public static ReviewDTO toReviewDTO(Review review) {
    ItemDTO itemDTO = review.getItem() != null ? ItemMapper.toItemDTO(review.getItem()) : null;

    return new ReviewDTO(review.getId(), review.getStars(), review.getDescription(), itemDTO);
  }
}
