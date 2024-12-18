package com.mth.eshop.controller;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.record.CartDTO;
import com.mth.eshop.service.CartService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final CartService service;

  public CartController(CartService service) {
    this.service = service;
  }

  @GetMapping()
  public ResponseEntity<?> showCart(
      @RequestParam Integer customerId, @RequestParam Integer cartId) {
    CartDTO cart;

    try {
      cart = service.showCart(cartId, customerId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @GetMapping("/create")
  public ResponseEntity<?> createCart() {
    CartDTO cart;

    try {
      cart = service.createCart();
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @PutMapping("/add-item")
  public ResponseEntity<?> addItemToCart(
      @RequestParam Integer customerId, @RequestParam Integer cartId, @RequestParam String itemId) {
    String message;

    try {
      message = service.addToCart(customerId, cartId, itemId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @DeleteMapping("/remove-item")
  public ResponseEntity<?> removeItem(
      @RequestParam Integer customerId, @RequestParam Integer cartId, @RequestParam String itemId) {
    String message;

    try {
      message = service.removeItemFromCart(customerId, cartId, itemId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @PatchMapping("/recalculate")
  public ResponseEntity<?> recalculate(
      @RequestParam Integer customerId, @RequestParam Integer cartId) {
    CartDTO cart;

    try {
      cart = service.recalculateCart(customerId, cartId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @PatchMapping("/add-coupon")
  public ResponseEntity<?> addCoupon(
      @RequestParam Integer customerId,
      @RequestParam Integer cartId,
      @RequestParam String couponId) {
    CartDTO cart;

    try {
      cart = service.addDiscountCoupon(customerId, cartId, couponId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @DeleteMapping("/remove-coupon")
  public ResponseEntity<?> removeCoupon(
      @RequestParam Integer customerId, @RequestParam Integer cartId) {
    CartDTO cart;

    try {
      cart = service.removeDiscountCoupon(customerId, cartId);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }
}
