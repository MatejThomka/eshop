package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.ReviewDTO;
import com.mth.eshop.model.Review;

public class ReviewMapper {
  public static ReviewDTO toReviewDTO(Review review) {
    return new ReviewDTO(review.getFromCustomer(), review.getStars(), review.getDescription());
  }
}
