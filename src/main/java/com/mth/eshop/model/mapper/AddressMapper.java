package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Address;
import com.mth.eshop.model.DTO.AddressDTO;
import com.mth.eshop.model.DTO.UserDTO;

public class AddressMapper {
  public static AddressDTO toAddressDTO(Address address) {
    UserDTO customer =
        address.getUser() != null ? UserMapper.toCustomerDTO(address.getUser()) : null;

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
