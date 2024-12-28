package com.mth.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String street;
  String number;
  String city;
  Integer zip;
  String country;
  @ManyToOne
  User user;
}
