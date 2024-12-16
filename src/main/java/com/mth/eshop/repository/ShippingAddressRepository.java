package com.mth.eshop.repository;

import com.mth.eshop.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {}
