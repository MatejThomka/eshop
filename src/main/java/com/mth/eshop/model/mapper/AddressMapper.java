package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Address;
import com.mth.eshop.model.record.AddressDTO;
import com.mth.eshop.model.record.CustomerDTO;

public class AddressMapper {
  public static AddressDTO toAddressDTO(Address address) {
    CustomerDTO customerDTO =
        address.getCustomer() != null ? CustomerMapper.toCustomerDTO(address.getCustomer()) : null;

    return new AddressDTO(
        address.getId(),
        address.getStreet(),
        address.getNumber(),
        address.getCity(),
        address.getZip(),
        address.getCountry(),
        customerDTO);
  }
}
