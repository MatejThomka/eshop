package com.mth.eshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
