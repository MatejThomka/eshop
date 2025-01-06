package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotNull;

public record UserDTO(
    @NotNull(message = "Customer ID cannot be null") Integer id,
    String firstname,
    String lastname,
    String email,
    String phone,
    boolean isTemporary,
    AddressDTO address,
    ShippingAddressDTO shippingAddress,
    CartDTO cart) {}
