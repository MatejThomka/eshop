package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.*;
import com.mth.eshop.model.User;
import java.util.List;

public class UserMapper {
  public static UserDTO toCustomerDTO(User user) {
    List<AddressDTO> addresses =
        user.getAddress().stream().map(AddressMapper::toAddressDTO).toList();
    List<ShippingAddressDTO> shippingAddresses =
        user.getShippingAddress().stream()
            .map(ShippingAddressMapper::toShippingAddressDTO)
            .toList();
    CartDTO cart = user.getCart() != null ? CartMapper.toCartDTO(user.getCart()) : null;

    return new UserDTO(
        user.getId(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getPhone(),
        user.isTemporary(),
        addresses,
        shippingAddresses,
        cart);
  }
}
