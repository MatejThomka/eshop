package com.mth.eshop.model.DTO;

import com.mth.eshop.model.Address;
import com.mth.eshop.model.ShippingAddress;

public record UpdateUserRequest(
    String firstname,
    String lastname,
    String phone,
    Address address,
    ShippingAddress shippingAddress) {}
