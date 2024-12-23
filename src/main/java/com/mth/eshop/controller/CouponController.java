package com.mth.eshop.controller;

import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.DTO.CouponDTO;
import com.mth.eshop.service.CouponService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  private final CouponService service;

  public CouponController(CouponService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<CouponDTO>> showAllCoupons() {
    List<CouponDTO> coupons = service.showAllCoupons();
    return ResponseEntity.ok(coupons);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CouponDTO> showCoupon(@PathVariable String id) {
    CouponDTO coupon = service.getCoupon(id);
    return ResponseEntity.ok(coupon);
  }

  @PostMapping("/add")
  public ResponseEntity<CouponDTO> addCoupon(@Valid @RequestBody Coupon coupon) {
    CouponDTO addedCoupon = service.createCoupon(coupon);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/coupon/{id}")
            .buildAndExpand(addedCoupon.id())
            .toUri();

    return ResponseEntity.created(location).body(addedCoupon);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<CouponDTO> deleteCoupon(@PathVariable String id) {
    CouponDTO coupon = service.removeCoupon(id);
    return ResponseEntity.ok(coupon);
  }
}
