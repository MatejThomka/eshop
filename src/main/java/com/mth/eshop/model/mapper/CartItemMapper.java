package com.mth.eshop.model.mapper;

import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.DTO.CartItemDTO;

public class CartItemMapper {
  public static CartItemDTO toCartItemDTO(CartItem cartItem) {

    double roundedPrice = Math.round(cartItem.getPrice() * 100.0) / 100.0;

    return new CartItemDTO(
        cartItem.getId(), cartItem.getName(), cartItem.getQuantity(), roundedPrice);
  }
}
