package com.mth.eshop.util;

import com.mth.eshop.model.Cart;
import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.Item;
import java.util.ArrayList;

public class CartHelper {

  public static void updateCartTotals(Cart cart) {
    int totalQuantity = cart.getCartItem().stream().mapToInt(CartItem::getQuantity).sum();
    double totalPrice = cart.getCartItem().stream().mapToDouble(CartItem::getPrice).sum();

    cart.setQuantity(totalQuantity);
    cart.setOriginalPrice(totalPrice);
    cart.setFinalPrice(totalPrice);
  }

  public static double calculateDiscount(Cart cart) {
    if (cart.getCoupon() == null) {
      return 0.0;
    }
    return cart.getOriginalPrice() * (cart.getCoupon().getDiscountInPercentage() / 100.0);
  }

  public static void applyDiscountAndUpdateCart(Cart cart) {
    double discount = calculateDiscount(cart);
    double totalPriceWithDiscount = cart.getOriginalPrice() - discount;
    cart.setFinalPrice(totalPriceWithDiscount);
  }

  public static boolean hasCoupon(Cart cart) {
    return cart.getCoupon() != null;
  }

  public static void ensureCartItemList(Cart cart) {
    if (cart.getCartItem() == null) {
      cart.setCartItem(new ArrayList<>());
    }
  }

  public static double calculateItemPrice(Item item, int quantity) {
    return item.getPrice() * quantity;
  }
}
