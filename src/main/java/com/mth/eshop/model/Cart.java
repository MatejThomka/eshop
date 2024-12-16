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
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @OneToOne Customer customer;
  @OneToMany List<CartItem> cartItem;
  Integer quantity;
  Double finalPrice;
  Double originalPrice;
  @ManyToOne Coupon coupon;
}
