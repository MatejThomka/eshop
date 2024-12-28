package com.mth.eshop.model.DTO;

public record AddressDTO(
    Integer id,
    String street,
    String number,
    String city,
    Integer zip,
    String country,
    UserDTO customer) {}
