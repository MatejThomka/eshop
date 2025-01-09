package com.mth.eshop.model;

import jakarta.persistence.*;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @OneToOne User user;

  @OneToMany(fetch = FetchType.EAGER)
  List<CartItem> cartItem;

  Integer quantity;
  Double finalPrice;
  Double originalPrice;
  @ManyToOne Coupon coupon;
}
