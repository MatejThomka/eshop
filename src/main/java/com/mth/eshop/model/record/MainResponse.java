package com.mth.eshop.model.record;

import java.util.List;

public record MainResponse(List<ItemDTO> items, CartDTO cart, List<CouponDTO> coupon) {}
