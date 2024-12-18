package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Review;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.model.record.ReviewDTO;

public class ReviewMapper {
  public static ReviewDTO toReviewDTO(Review review) {
    return new ReviewDTO(review.getFromCustomer(), review.getStars(), review.getDescription());
  }
}
