package com.mth.eshop.model.mapper;

import com.mth.eshop.model.ShippingAddress;
import com.mth.eshop.model.record.CustomerDTO;
import com.mth.eshop.model.record.ShippingAddressDTO;

public class ShippingAddressMapper {
  public static ShippingAddressDTO toShippingAddressDTO(ShippingAddress shippingAddress) {
    CustomerDTO customerDTO =
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
        customerDTO);
  }
}
