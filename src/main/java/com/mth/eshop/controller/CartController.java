package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.CartCouponRequest;
import com.mth.eshop.model.DTO.CartDTO;
import com.mth.eshop.model.DTO.CartItemRequest;
import com.mth.eshop.service.CartService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final CartService service;

  public CartController(CartService service) {
    this.service = service;
  }

  @GetMapping("/{customerId}/{cartId}")
  public ResponseEntity<CartDTO> showCart(
      @PathVariable Integer customerId, @PathVariable Integer cartId) {
    CartDTO cart = service.showCart(cartId, customerId);
    return ResponseEntity.ok(cart);
  }

  @GetMapping("/create")
  public ResponseEntity<CartDTO> createCart() {
    CartDTO cart = service.createCart();

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/cart/{customerId}/{cartId}")
            .buildAndExpand(cart.customerId(), cart.id())
            .toUri();

    return ResponseEntity.created(location).body(cart);
  }

  @PutMapping("/{id}/add-item")
  public ResponseEntity<CartDTO> addItemToCart(
      @PathVariable Integer id, @Valid @RequestBody CartItemRequest request) {
    CartDTO updatedCart = service.addToCart(request.customerId(), id, request.itemId());
    return ResponseEntity.ok(updatedCart);
  }

  @DeleteMapping("/{id}/remove-item")
  public ResponseEntity<CartDTO> removeItem(
      @PathVariable Integer id, @Valid @RequestBody CartItemRequest request) {
    CartDTO cart = service.removeItemFromCart(request.customerId(), id, request.itemId());
    return ResponseEntity.ok(cart);
  }

  @PatchMapping("/{customerId}/{cartId}/recalculate")
  public ResponseEntity<CartDTO> recalculate(
      @PathVariable Integer customerId, @PathVariable Integer cartId) {
    CartDTO cart = service.updateCartWithOrWithoutDiscount(customerId, cartId);
    return ResponseEntity.ok(cart);
  }

  @PatchMapping("/{id}/add-coupon")
  public ResponseEntity<CartDTO> addCoupon(
      @PathVariable Integer id, @Valid @RequestBody CartCouponRequest request) {
    CartDTO cart = service.addDiscountCoupon(request.customerId(), id, request.couponId());
    return ResponseEntity.ok(cart);
  }

  @DeleteMapping("/{id}/remove-coupon")
  public ResponseEntity<CartDTO> removeCoupon(
      @PathVariable Integer id, @Valid @RequestBody CartCouponRequest request) {
    CartDTO cart = service.removeDiscountCoupon(request.customerId(), id);
    return ResponseEntity.ok(cart);
  }
}
