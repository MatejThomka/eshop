package com.mth.eshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  String fromCustomer;

  @NotNull(message = "Review Stars cannot be null")
  Double stars;

  String description;
  @ManyToOne Item item;
}
