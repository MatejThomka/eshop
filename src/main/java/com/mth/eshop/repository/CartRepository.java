package com.mth.eshop.repository;

import com.mth.eshop.model.Cart;

import java.util.List;
import java.util.Optional;

import com.mth.eshop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
  Optional<Cart> findCartByIdAndCustomer_Id(Integer cartId, Integer customerId);

  List<Cart> findByCoupon(Coupon coupon);
}
