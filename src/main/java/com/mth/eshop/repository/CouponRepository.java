package com.mth.eshop.repository;

import com.mth.eshop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {}
