package com.mth.eshop.model;

import jakarta.persistence.*;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotBlank(message = "Item Name cannot be blank")
  String name;

  @NotNull(message = "Item Price cannot be null")
  Double price;

  String description;
  Integer stockQuantity;
  Double stars;
  @OneToMany List<Review> review;
}
