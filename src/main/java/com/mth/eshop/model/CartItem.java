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
public class CartItem {
  @Id String id;

  String name;
  Integer quantity;
  Double price;
  @ManyToOne Cart cart;
}
