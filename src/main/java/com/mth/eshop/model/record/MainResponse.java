package com.mth.eshop.model.record;

import java.util.List;

public record MainResponse(List<ItemsDTO> items, CartDTO cart, List<CouponDTO> coupons) {}
