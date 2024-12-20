package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Coupon;
import com.mth.eshop.model.DTO.CouponDTO;

public class CouponMapper {
  public static CouponDTO toCouponDTO(Coupon coupon) {
    return new CouponDTO(coupon.getId(), coupon.getDiscountInPercentage());
  }
}
