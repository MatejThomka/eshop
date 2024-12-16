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
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  Double stars;
  String description;
  @ManyToOne Item item;
}
