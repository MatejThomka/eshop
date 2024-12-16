package com.mth.eshop.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String firstname;
  String lastname;
  String email;
  String password;
  boolean isTemporary;
  @OneToMany List<Address> address;
  @OneToMany List<ShippingAddress> shippingAddress;
  @OneToOne Cart cart;
}
