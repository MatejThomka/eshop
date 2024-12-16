package com.mth.eshop.repository;

import com.mth.eshop.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
  Optional<CartItem> findCartItemByIdAndCart_Id(String itemId, Integer cartId);
}
