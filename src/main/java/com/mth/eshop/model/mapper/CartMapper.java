package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Cart;
import com.mth.eshop.model.DTO.CartDTO;
import com.mth.eshop.model.DTO.CartItemDTO;
import java.util.List;

public class CartMapper {
  public static CartDTO toCartDTO(Cart cart) {
    List<CartItemDTO> items =
        cart.getCartItem().stream().map(CartItemMapper::toCartItemDTO).toList();

    Integer discountInPercentage =
        cart.getCoupon() != null
            ? CouponMapper.toCouponDTO(cart.getCoupon()).discountInPercentage()
            : null;

    double roundedPrice = Math.round(cart.getFinalPrice() * 100.0) / 100.0;

    return new CartDTO(
        cart.getId(),
        cart.getUser().getId(),
        items,
        cart.getQuantity(),
        roundedPrice,
        discountInPercentage);
  }
}
