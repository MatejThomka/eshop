package com.mth.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Coupon {
  @Id String id;
  Integer discountInPercentage;
  @OneToMany List<Cart> cart;
}
