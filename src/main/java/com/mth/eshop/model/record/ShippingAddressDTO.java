package com.mth.eshop.model.record;

public record ShippingAddressDTO(
    Integer id,
    String street,
    String number,
    String city,
    Integer zip,
    String country,
    CustomerDTO customer) {}
