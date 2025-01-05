package com.mth.eshop.repository;

import com.mth.eshop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
  boolean existsItemById(String id);
}
