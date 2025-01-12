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
public class ShippingAddress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String street;
  String number;
  String city;
  Integer zip;
  String country;
  @OneToOne User user;
}
