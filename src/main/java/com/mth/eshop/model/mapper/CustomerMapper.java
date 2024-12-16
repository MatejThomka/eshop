package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Customer;
import com.mth.eshop.model.record.AddressDTO;
import com.mth.eshop.model.record.CartDTO;
import com.mth.eshop.model.record.CustomerDTO;
import com.mth.eshop.model.record.ShippingAddressDTO;
import java.util.List;

public class CustomerMapper {
  public static CustomerDTO toCustomerDTO(Customer customer) {
    List<AddressDTO> addressDTOS =
        customer.getAddress().stream().map(AddressMapper::toAddressDTO).toList();
    List<ShippingAddressDTO> shippingAddressDTOS =
        customer.getShippingAddress().stream()
            .map(ShippingAddressMapper::toShippingAddressDTO)
            .toList();
    CartDTO cartDTO = customer.getCart() != null ? CartMapper.toCartDTO(customer.getCart()) : null;

    return new CustomerDTO(
        customer.getId(),
        customer.getFirstname(),
        customer.getLastname(),
        customer.getEmail(),
        customer.isTemporary(),
        addressDTOS,
        shippingAddressDTOS,
        cartDTO);
  }
}
