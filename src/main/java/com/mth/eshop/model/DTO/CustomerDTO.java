package com.mth.eshop.model.DTO;

import java.util.List;

public record CustomerDTO(
    Integer id,
    String firstname,
    String lastname,
    String email,
    boolean isTemporary,
    List<AddressDTO> addresses,
    List<ShippingAddressDTO> shippingAddresses,
    CartDTO cart) {}
