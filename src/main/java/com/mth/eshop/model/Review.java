package com.mth.eshop.model;

import jakarta.persistence.*;
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
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String fromCustomer;
  @NotNull(message = "Review Stars cannot be null") Double stars;
  String description;
  @ManyToOne Item item;
}
