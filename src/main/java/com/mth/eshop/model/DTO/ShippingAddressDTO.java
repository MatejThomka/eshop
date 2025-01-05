package com.mth.eshop.model.DTO;

public record ShippingAddressDTO(
    Integer id,
    String street,
    String number,
    String city,
    Integer zip,
    String country,
    UserDTO customer) {}
