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
public class Item {
  @Id String id;

  String name;
  Double price;
  String description;
  Integer stockQuantity;
  Double stars;
  @OneToMany List<Review> review;
}
