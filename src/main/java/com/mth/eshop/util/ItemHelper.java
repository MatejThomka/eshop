package com.mth.eshop.util;

import com.mth.eshop.exception.ReviewException;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.Review;
import com.mth.eshop.repository.ReviewRepository;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ItemHelper {

  public static void validateReviewStars(double stars) throws ReviewException {
    if (stars < 0.0 || stars > 5.0) {
      throw new ReviewException(
          "Invalid star rating. Must be between 0 and 5.", HttpStatus.BAD_REQUEST);
    }
  }

  public static void updateExistingItem(Item existingItem, Item newItem) {
    existingItem.setName(newItem.getName());
    existingItem.setPrice(newItem.getPrice());
    existingItem.setDescription(newItem.getDescription());
    existingItem.setStockQuantity(newItem.getStockQuantity());
  }

  public static void createNewItem(Item item) {
    item.setReview(List.of());
  }

  public static void updateExistingReview(Review existingReview, Review newReview) {
    existingReview.setStars(newReview.getStars());
    existingReview.setDescription(newReview.getDescription());
  }

  public static void addNewReview(Item item, Review review, ReviewRepository reviewRepository) {
    Review newReview = new Review();
    newReview.setFromCustomer(review.getFromCustomer());
    newReview.setStars(review.getStars());
    newReview.setDescription(review.getDescription());
    newReview.setItem(item);

    Review savedReview = reviewRepository.save(newReview);

    item.getReview().add(savedReview);
  }

  public static void updateAverageStars(Item item) {
    double averageStars =
        item.getReview().stream().mapToDouble(Review::getStars).average().orElse(0.0);
    item.setStars(averageStars);
  }
}
