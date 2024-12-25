package com.mth.eshop.service;

import static com.mth.eshop.util.CartHelper.*;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.CouponException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.model.*;
import com.mth.eshop.model.DTO.CartDTO;
import com.mth.eshop.model.mapper.CartMapper;
import com.mth.eshop.repository.*;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
  private final CartRepository cartRepository;
  private final ItemRepository itemRepository;
  private final CartItemRepository cartItemRepository;
  private final CustomerRepository customerRepository;
  private final CouponRepository couponRepository;

  public CartService(
      CartRepository cartRepository,
      ItemRepository itemRepository,
      CartItemRepository cartItemRepository,
      CustomerRepository customerRepository,
      CouponRepository couponRepository) {
    this.cartRepository = cartRepository;
    this.itemRepository = itemRepository;
    this.cartItemRepository = cartItemRepository;
    this.customerRepository = customerRepository;
    this.couponRepository = couponRepository;
  }

  @Transactional
  public CartDTO createCart() {
    Customer temporaryCustomer = new Customer();
    temporaryCustomer.setTemporary(true);
    customerRepository.save(temporaryCustomer);

    Cart cart = new Cart();
    cart.setCustomer(temporaryCustomer);
    ensureCartItemList(cart);
    cart.setQuantity(0);
    cart.setFinalPrice(0.0);
    cart.setCoupon(null);

    cartRepository.save(cart);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional(readOnly = true)
  public CartDTO showCart(Integer cartId, Integer customerId) throws EshopException {
    return CartMapper.toCartDTO(findCart(cartId, customerId));
  }

  @Transactional
  public CartDTO addToCart(Integer customerId, Integer cartId, String itemId)
      throws EshopException {
    Cart cart = findCart(cartId, customerId);
    ensureCartItemList(cart);
    Item item = findItem(itemId);

    if (item.getStockQuantity() == 0) {
      throw new ItemException("Item with ID: " + itemId + " is sold out!", HttpStatus.BAD_REQUEST);
    }

    Optional<CartItem> cartItemOptional =
        cartItemRepository.findCartItemByIdAndCart_Id(itemId, cartId);

    if (cartItemOptional.isEmpty()) {
      CartItem newCartItem = new CartItem(item.getId(), item.getName(), 1, item.getPrice(), cart);
      cartItemRepository.save(newCartItem);
      cart.getCartItem().add(newCartItem);
    } else {
      CartItem existingCartItem = cartItemOptional.get();
      existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
      existingCartItem.setPrice(calculateItemPrice(item, existingCartItem.getQuantity()));
      cartItemRepository.save(existingCartItem);
    }

    finalizeCartUpdates(cart);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional
  public CartDTO removeItemFromCart(Integer customerId, Integer cartId, String itemId)
      throws EshopException {
    Cart cart = findCart(cartId, customerId);
    ensureCartItemList(cart);
    CartItem cartItem = findCartItem(itemId, cart.getId());
    Item item = findItem(itemId);

    if (cartItem.getQuantity() > 1) {
      cartItem.setQuantity(cartItem.getQuantity() - 1);
      cartItem.setPrice(calculateItemPrice(item, cartItem.getQuantity()));
      cartItemRepository.save(cartItem);
    } else {
      cart.getCartItem().remove(cartItem);
      cartItemRepository.delete(cartItem);
    }

    finalizeCartUpdates(cart);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional
  public CartDTO updateCartWithOrWithoutDiscount(Integer customerId, Integer cartId)
      throws EshopException {
    Cart cart = findCart(cartId, customerId);

    if (hasCoupon(cart)) {
      applyDiscountAndUpdateCart(cart);
    } else {
      updateCartTotals(cart);
    }

    cartRepository.save(cart);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional
  public CartDTO addDiscountCoupon(Integer customerId, Integer cartId, String couponId)
      throws EshopException {
    Cart cart = findCart(cartId, customerId);
    Coupon newCoupon = findCoupon(couponId);
    Coupon oldCoupon = cart.getCoupon();

    cart.setCoupon(null);

    if (oldCoupon != null) {
      oldCoupon.getCart().remove(cart);
      couponRepository.save(oldCoupon);
    }

    cart.setCoupon(newCoupon);

    if (hasCoupon(cart)) {
      applyDiscountAndUpdateCart(cart);
    }

    cartRepository.save(cart);
    newCoupon.getCart().add(cart);
    couponRepository.save(newCoupon);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional
  public CartDTO removeDiscountCoupon(Integer customerId, Integer cartId) throws EshopException {
    Cart cart = findCart(cartId, customerId);
    Coupon coupon = cart.getCoupon();
    if (coupon == null) {
      throw new CouponException("No coupon found in the cart to remove!", HttpStatus.NOT_FOUND);
    }

    cart.setCoupon(null);
    updateCartTotals(cart);

    cartRepository.save(cart);
    coupon.getCart().remove(cart);
    couponRepository.save(coupon);

    return CartMapper.toCartDTO(cart);
  }

  @Transactional(readOnly = true)
  protected Cart findCart(Integer cartId, Integer customerId) throws EshopException {
    return cartRepository
        .findCartByIdAndCustomer_Id(cartId, customerId)
        .orElseThrow(
            () ->
                new CartException(
                    "Cart ID: " + cartId + " with customer ID: " + customerId + " doesn't exist!",
                    HttpStatus.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  protected Item findItem(String itemId) throws EshopException {
    return itemRepository
        .findById(itemId)
        .orElseThrow(
            () -> new ItemException("Item doesn't exist with ID: " + itemId, HttpStatus.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  protected Coupon findCoupon(String couponId) throws EshopException {
    return couponRepository
        .findById(couponId)
        .orElseThrow(() -> new CouponException("Coupon doesn't exist!", HttpStatus.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  protected CartItem findCartItem(String itemId, Integer cartId) throws EshopException {
    return cartItemRepository
        .findCartItemByIdAndCart_Id(itemId, cartId)
        .orElseThrow(
            () -> new ItemException("Item doesn't exist with ID: " + itemId, HttpStatus.NOT_FOUND));
  }

  @Transactional
  protected void finalizeCartUpdates(Cart cart) {
    updateCartTotals(cart);
    if (hasCoupon(cart)) {
      applyDiscountAndUpdateCart(cart);
    }
    cartRepository.save(cart);
  }
}
