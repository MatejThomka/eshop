package com.mth.eshop.service;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.exception.ReviewException;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.Review;
import com.mth.eshop.model.mapper.ItemMapper;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.repository.ItemRepository;
import com.mth.eshop.repository.ReviewRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final ReviewRepository reviewRepository;

  public ItemService(ItemRepository itemRepository, ReviewRepository reviewRepository) {
    this.itemRepository = itemRepository;
    this.reviewRepository = reviewRepository;
  }

  public ItemDTO getItem(String id) throws EshopException {
    Optional<Item> itemOptional = itemRepository.findById(id);

    if (itemOptional.isEmpty()) {
      throw new ItemException("Item doesn't exists with ID: " + id, HttpStatus.NOT_FOUND);
    }

    Item item = itemOptional.get();

    return ItemMapper.toItemDTO(item);
  }

  public String createOrUpdateItem(Item item) throws EshopException {
    Optional<Item> itemOptional = itemRepository.findById(item.getId());

    if (itemOptional.isPresent()) {
      Item updatedItem = itemOptional.get();
      updatedItem.setName(item.getName());
      updatedItem.setPrice(item.getPrice());
      updatedItem.setDescription(item.getDescription());
      updatedItem.setStockQuantity(item.getStockQuantity());
      itemRepository.save(updatedItem);
      return "Item updated successfully.";
    } else {
      Item newItem = new Item();
      newItem.setId(item.getId());
      newItem.setName(item.getName());
      newItem.setPrice(item.getPrice());
      newItem.setDescription(item.getDescription());
      newItem.setStockQuantity(item.getStockQuantity());
      newItem.setReview(List.of());
      itemRepository.save(newItem);
    }

    return "Item add successfully.";
  }

  public String removeItem(String id) throws EshopException {
      Optional<Item> itemOptional = itemRepository.findById(id);

      if (itemOptional.isEmpty()) {
          throw new ItemException("Item doesn't exists with ID: " + id, HttpStatus.NOT_FOUND);
      }

      Item item = itemOptional.get();
      itemRepository.delete(item);

      return "Item delete successfully.";
  }

  public ItemDTO addOrUpdateReview(String id, Review review) throws EshopException {
      Optional<Item> itemOptional = itemRepository.findById(id);

      if (review.getStars() > 5.0 || review.getStars() < 0.0) {
          throw new ReviewException("You set wrong stars score!", HttpStatus.NOT_ACCEPTABLE);
      }

      if (itemOptional.isEmpty()) {
          throw new ItemException("Item doesn't exists with ID: " + id, HttpStatus.NOT_FOUND);
      }

      Item item = itemOptional.get();

      Optional<Review> reviewOptional = reviewRepository.findByIdAndItem_Id(review.getId(), item.getId());

      if (reviewOptional.isPresent()) {
          Review updatedReview = reviewOptional.get();
          updatedReview.setStars(review.getStars());
          updatedReview.setDescription(review.getDescription());
          reviewRepository.save(updatedReview);
      } else {
          Review newReview = new Review();
          newReview.setFromCustomer(review.getFromCustomer());
          newReview.setStars(review.getStars());
          newReview.setDescription(review.getDescription());
          newReview.setItem(item);
          reviewRepository.save(newReview);

          item.getReview().add(newReview);
          itemRepository.save(item);
      }

      item.setStars(item.getReview().stream().mapToDouble(Review::getStars).sum() / item.getReview().size());
      itemRepository.save(item);

      return ItemMapper.toItemDTO(item);
  }
}
