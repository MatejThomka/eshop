package com.mth.eshop.service;

import static com.mth.eshop.util.GlobalHelper.validateAccess;
import static com.mth.eshop.util.GlobalHelper.validateAccessForAdmin;

import com.mth.eshop.exception.CouponException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Cart;
import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.DTO.CouponDTO;
import com.mth.eshop.model.mapper.CouponMapper;
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

  public CouponDTO createCoupon(Coupon coupon) throws EshopException {
    validateAccessForAdmin();

    validateCouponDoesNotExist(coupon.getId());

    couponRepository.save(coupon);

    return CouponMapper.toCouponDTO(coupon);
  }

  public CouponDTO removeCoupon(String couponId) throws EshopException {
    validateAccessForAdmin();

    Coupon coupon = findCoupon(couponId);

    removeCouponFromCarts(coupon);

    couponRepository.delete(coupon);

    return CouponMapper.toCouponDTO(coupon);
  }

  public CouponDTO getCoupon(String id) {
    Optional<Coupon> couponOptional = couponRepository.findById(id);

    if (couponOptional.isEmpty()) {
      throw new CouponException("Coupon doesn't exists!", HttpStatus.NOT_FOUND);
    }

    Coupon coupon = couponOptional.get();

    return CouponMapper.toCouponDTO(coupon);
  }

  private void validateCouponDoesNotExist(String id) throws EshopException {
    if (couponRepository.findById(id).isPresent()) {
      throw new CouponException("Coupon is already exists!", HttpStatus.CONFLICT);
    }
  }

  private Coupon findCoupon(String id) throws EshopException {
    return couponRepository
        .findById(id)
        .orElseThrow(() -> new CouponException("Coupon doesn't exists!", HttpStatus.NOT_FOUND));
  }

  private void removeCouponFromCarts(Coupon coupon) {
    List<Cart> cartWithCoupon = cartRepository.findByCoupon(coupon);

    cartWithCoupon.forEach(
        cart -> {
          cart.setCoupon(null);
          cart.setFinalPrice(cart.getOriginalPrice());
        });

    cartRepository.saveAll(cartWithCoupon);
  }
}
