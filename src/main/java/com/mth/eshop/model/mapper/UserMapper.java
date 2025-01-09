package com.mth.eshop.model.mapper;

import com.mth.eshop.model.DTO.*;
import com.mth.eshop.model.User;
import java.util.List;

public class UserMapper {
  public static UserDTO toCustomerDTO(User user) {
    CartDTO cart = user.getCart() != null ? CartMapper.toCartDTO(user.getCart()) : null;
    AddressDTO address =
        user.getAddress() != null ? AddressMapper.toAddressDTO(user.getAddress()) : null;
    ShippingAddressDTO shippingAddress =
        user.getShippingAddress() != null
            ? ShippingAddressMapper.toShippingAddressDTO(user.getShippingAddress())
            : null;

    return new UserDTO(
        user.getId(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getPhone(),
        user.isTemporary(),
        address,
        shippingAddress,
        cart);
  }
}
