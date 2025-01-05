package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.ShippingAddressDTO;
import com.mth.eshop.model.DTO.UserDTO;
import com.mth.eshop.model.ShippingAddress;

public class ShippingAddressMapper {
  public static ShippingAddressDTO toShippingAddressDTO(ShippingAddress shippingAddress) {
    UserDTO customer =
        shippingAddress.getUser() != null
            ? UserMapper.toCustomerDTO(shippingAddress.getUser())
            : null;

    return new ShippingAddressDTO(
        shippingAddress.getId(),
        shippingAddress.getStreet(),
        shippingAddress.getNumber(),
        shippingAddress.getCity(),
        shippingAddress.getZip(),
        shippingAddress.getCountry(),
        customer);
  }
}
