package com.mth.eshop.repository;

import com.mth.eshop.model.Cart;
import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
  Optional<Cart> findCartByIdAndCustomer_Id(Integer cartId, Integer customerId);

  List<Cart> findByCoupon(Coupon coupon);

  List<Cart> findAllByCartItemContaining(CartItem cartItem);
}
