package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.CustomerDTO;
import com.mth.eshop.model.DTO.ShippingAddressDTO;
import com.mth.eshop.model.ShippingAddress;

public class ShippingAddressMapper {
  public static ShippingAddressDTO toShippingAddressDTO(ShippingAddress shippingAddress) {
    CustomerDTO customer =
        shippingAddress.getCustomer() != null
            ? CustomerMapper.toCustomerDTO(shippingAddress.getCustomer())
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
