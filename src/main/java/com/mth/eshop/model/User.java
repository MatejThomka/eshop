package com.mth.eshop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
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
