package com.mth.eshop.service;

import com.mth.eshop.exception.CouponException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Cart;
import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.mapper.CouponMapper;
import com.mth.eshop.model.record.CouponDTO;
import com.mth.eshop.repository.CartRepository;
import com.mth.eshop.repository.CouponRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

  private final CouponRepository couponRepository;
  private final CartRepository cartRepository;

  public CouponService(CouponRepository couponRepository, CartRepository cartRepository) {
    this.couponRepository = couponRepository;
    this.cartRepository = cartRepository;
  }

  public List<CouponDTO> showAllCoupons() {
    return couponRepository.findAll().stream().map(CouponMapper::toCouponDTO).toList();
  }

  public String createCoupon(Coupon coupon) throws EshopException {
    Optional<Coupon> couponOptional = couponRepository.findById(coupon.getId());

    if (couponOptional.isPresent()) {
      throw new CouponException("Coupon is already exists!", HttpStatus.CONFLICT);
    }

    couponRepository.save(coupon);

    return "Coupon add successfully with ID: " + coupon.getId();
  }

  public String removeCoupon(String couponId) throws EshopException {
    Optional<Coupon> couponOptional = couponRepository.findById(couponId);

    if (couponOptional.isEmpty()) {
      throw new CouponException("Coupon doesn't exists!", HttpStatus.NOT_FOUND);
    }

    Coupon coupon = couponOptional.get();

    List<Cart> cartWithCoupon = cartRepository.findByCoupon(coupon);

    for (Cart cart : cartWithCoupon) {
      cart.setCoupon(null);
      cart.setFinalPrice(cart.getOriginalPrice());
      cartRepository.save(cart);
    }

    couponRepository.delete(coupon);

    return "Coupon deleted successfully.";
  }
}
