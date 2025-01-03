package com.mth.eshop.service;

import static com.mth.eshop.util.GlobalHelper.validateAccess;
import static com.mth.eshop.util.GlobalHelper.validateAccessForAdmin;
import static com.mth.eshop.util.ItemHelper.*;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.model.Cart;
import com.mth.eshop.model.CartItem;
import com.mth.eshop.model.DTO.ItemDTO;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.Review;
import com.mth.eshop.model.mapper.ItemMapper;
import com.mth.eshop.repository.CartItemRepository;
import com.mth.eshop.repository.CartRepository;
import com.mth.eshop.repository.ItemRepository;
import com.mth.eshop.repository.ReviewRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final ReviewRepository reviewRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  public ItemService(
      ItemRepository itemRepository,
      ReviewRepository reviewRepository,
      CartRepository cartRepository,
      CartItemRepository cartItemRepository) {
    this.itemRepository = itemRepository;
    this.reviewRepository = reviewRepository;
    this.cartRepository = cartRepository;
    this.cartItemRepository = cartItemRepository;
  }

  @Transactional
  public ItemDTO getItem(String id) throws EshopException {
    Item item = findItem(id);

    return ItemMapper.toItemDTO(item);
  }

  public ItemDTO createOrUpdateItem(Item item) throws EshopException {
    validateAccessForAdmin();
    Optional<Item> itemOptional = itemRepository.findById(item.getId());
    if (itemOptional.isPresent()) {
      Item existingItem = itemOptional.get();
      updateExistingItem(existingItem, item);
      return ItemMapper.toItemDTO(itemRepository.save(existingItem));
    } else {
      createNewItem(item);
      return ItemMapper.toItemDTO(itemRepository.save(item));
    }
  }

  @Transactional
  public ItemDTO removeItem(String id) throws EshopException {
    validateAccessForAdmin();
    Item item = findItem(id);

    List<CartItem> cartItems = cartItemRepository.findAllById(id);
    List<Cart> carts = cartRepository.findAllByCartItemIn(cartItems);
    carts.forEach(cart -> cart.getCartItem().removeIf(cartItem -> cartItem.getId().equals(id)));
    cartRepository.saveAll(carts);

    cartItemRepository.deleteAll(cartItems);
    itemRepository.delete(item);

    return ItemMapper.toItemDTO(item);
  }

  @Transactional
  public ItemDTO addOrUpdateReview(String id, Review review) throws EshopException {
    validateReviewStars(review.getStars());

    Item item = findItem(id);

    Optional<Review> reviewOptional = reviewRepository.findByIdAndItem_Id(review.getId(), id);
    if (reviewOptional.isPresent()) {
      Review existingReview = reviewOptional.get();
      updateExistingReview(existingReview, review);
      reviewRepository.save(existingReview);
    } else {
      addNewReview(item, review, reviewRepository);
    }

    updateAverageStars(item);
    itemRepository.save(item);

    return ItemMapper.toItemDTO(item);
  }

  @Transactional(readOnly = true)
  protected Item findItem(String id) throws ItemException {
    return itemRepository
        .findById(id)
        .orElseThrow(
            () -> new ItemException("Item doesn't exist with ID: " + id, HttpStatus.NOT_FOUND));
  }
}
