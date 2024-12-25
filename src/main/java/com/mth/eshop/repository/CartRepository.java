package com.mth.eshop.repository;

import com.mth.eshop.model.Cart;
import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
  Optional<Cart> findCartByIdAndCustomer_Id(Integer cartId, Integer customerId);

  List<Cart> findByCoupon(Coupon coupon);

  @Query("SELECT c FROM Cart c JOIN c.cartItem ci WHERE ci IN :cartItems")
  List<Cart> findAllByCartItemIn(@Param("cartItems") List<CartItem> cartItems);
}
