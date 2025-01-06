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
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String firstname;
  String lastname;
  String email;
  String password;
  String phone;
  Role role;
  boolean isTemporary;
  @OneToOne Address address;
  @OneToOne ShippingAddress shippingAddress;
  @OneToOne Cart cart;
}
