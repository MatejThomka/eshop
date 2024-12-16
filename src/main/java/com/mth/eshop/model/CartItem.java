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
public class CartItem {
  @Id String id;

  String name;
  Integer quantity;
  Double price;
  @ManyToOne Cart cart;
}
