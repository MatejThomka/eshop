package com.mth.eshop.model.DTO;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UserDTO(
    @NotNull(message = "Customer ID cannot be null") Integer id,
    String firstname,
    String lastname,
    String email,
    String phone,
    boolean isTemporary,
    List<AddressDTO> addresses,
    List<ShippingAddressDTO> shippingAddresses,
    CartDTO cart) {}
