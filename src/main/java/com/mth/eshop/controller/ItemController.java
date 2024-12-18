package com.mth.eshop.controller;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.Review;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController (ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<?> showItem(@RequestParam String id) {
        ItemDTO item;

        try{
            item = itemService.getItem(id);
        } catch (EshopException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/add-or-update")
    public ResponseEntity<?> addOrUpdateItem(@RequestBody Item item) {
        String message;

        try{
            message = itemService.createOrUpdateItem(item);
        } catch (EshopException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteItem(@RequestParam String id) {
        String message;

        try{
            message = itemService.removeItem(id);
        } catch (EshopException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/review/add-or-update")
    public ResponseEntity<?> addReview(@RequestParam String id, @RequestBody Review review) {
        ItemDTO item;

        try{
            item = itemService.addOrUpdateReview(id, review);
        } catch (EshopException e) {
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
