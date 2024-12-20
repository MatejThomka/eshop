package com.mth.eshop.model.DTO;

import java.util.List;

public record MainResponse(List<ItemsDTO> items, CartDTO cart, List<CouponDTO> coupons) {}
