package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Address;
import com.mth.eshop.model.DTO.AddressDTO;
import com.mth.eshop.model.DTO.CustomerDTO;

public class AddressMapper {
  public static AddressDTO toAddressDTO(Address address) {
    CustomerDTO customer =
        address.getCustomer() != null ? CustomerMapper.toCustomerDTO(address.getCustomer()) : null;

    return new AddressDTO(
        address.getId(),
        address.getStreet(),
        address.getNumber(),
        address.getCity(),
        address.getZip(),
        address.getCountry(),
        customer);
  }
}
