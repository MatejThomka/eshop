package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.ItemDTO;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.Review;
import com.mth.eshop.service.ItemService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/item")
public class ItemController {

  private final ItemService service;

  public ItemController(ItemService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ItemDTO> showItem(@PathVariable String id) {
    ItemDTO item = service.getItem(id);
    return ResponseEntity.ok(item);
  }

  @PutMapping("/add-or-update")
  public ResponseEntity<ItemDTO> addOrUpdateItem(@Valid @RequestBody Item item) {
    ItemDTO updatedItem = service.createOrUpdateItem(item);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/item/{id}")
            .buildAndExpand(updatedItem.id())
            .toUri();

    return ResponseEntity.created(location).body(updatedItem);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteItem(@PathVariable String id) {
    ItemDTO deletedItem = service.removeItem(id);
    return ResponseEntity.ok(deletedItem);
  }

  @PutMapping("/{id}/review/add-or-update")
  public ResponseEntity<ItemDTO> addReview(
      @PathVariable String id, @Valid @RequestBody Review review) {
    ItemDTO item = service.addOrUpdateReview(id, review);
    return ResponseEntity.ok(item);
  }
}
