package com.mth.eshop.repository;

import com.mth.eshop.model.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
  Optional<CartItem> findCartItemByIdAndCart_Id(String itemId, Integer cartId);

  List<CartItem> findAllById(String itemId);
}
