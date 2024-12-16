package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Cart;
import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.record.CartDTO;
import com.mth.eshop.model.record.CartItemDTO;
import com.mth.eshop.model.record.CouponDTO;

import java.util.List;

public class CartMapper {
  public static CartDTO toCartDTO(Cart cart) {
    List<CartItemDTO> itemDTOS =
        cart.getCartItem().stream().map(CartItemMapper::toCartItemDTO).toList();

    Integer discountInPercentage = cart.getCoupon() != null ? CouponMapper.toCouponDTO(cart.getCoupon()).discountInPercentage() : null;

    double roundedPrice = Math.round(cart.getFinalPrice() * 100.0) / 100.0;

    return new CartDTO(
        cart.getId(),
        cart.getCustomer().getId(),
        itemDTOS,
        cart.getQuantity(),
        roundedPrice,
        discountInPercentage);
  }
}
