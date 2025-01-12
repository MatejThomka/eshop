package com.mth.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Coupon {
  @NotBlank(message = "Coupon ID cannot be blank")
  @Id
  String id;

  @NotNull(message = "Coupon Percentage cannot be null")
  Integer discountInPercentage;

  @OneToMany List<Cart> cart;
}
