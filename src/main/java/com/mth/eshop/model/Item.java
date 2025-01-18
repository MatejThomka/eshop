package com.mth.eshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
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
