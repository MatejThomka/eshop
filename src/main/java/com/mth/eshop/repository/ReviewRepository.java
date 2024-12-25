package com.mth.eshop.repository;

import com.mth.eshop.model.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  Optional<Review> findByIdAndItem_Id(Integer reviewId, String itemId);
}
