package com.mth.eshop.controller;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.record.CouponDTO;
import com.mth.eshop.service.CouponService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  private final CouponService couponService;

  public CouponController(CouponService couponService) {
    this.couponService = couponService;
  }

  @GetMapping
  public ResponseEntity<?> showAllCoupons() {
    List<CouponDTO> coupons;

    try {
      coupons = couponService.showAllCoupons();
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(coupons, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {
    String message;

    try {
      message = couponService.createCoupon(coupon);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteCoupon(@RequestParam String id) {
    String message;

    try {
      message = couponService.removeCoupon(id);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
