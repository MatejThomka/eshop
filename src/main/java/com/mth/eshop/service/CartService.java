package com.mth.eshop.service;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.CouponException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.model.*;
import com.mth.eshop.model.mapper.CartMapper;
import com.mth.eshop.model.record.CartDTO;
import com.mth.eshop.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartService {
  private final CartRepository cartRepository;
  private final ItemRepository itemRepository;
  private final CartItemRepository cartItemRepository;
  private final CustomerRepository customerRepository;
  private final CouponRepository couponRepository;

  public CartService(
      CartRepository cartRepository,
      ItemRepository itemRepository,
      CartItemRepository cartItemRepository,
      CustomerRepository customerRepository,
      CouponRepository couponRepository) {
    this.cartRepository = cartRepository;
    this.itemRepository = itemRepository;
    this.cartItemRepository = cartItemRepository;
    this.customerRepository = customerRepository;
    this.couponRepository = couponRepository;
  }

  public CartDTO createCart() {
    Customer temporaryCustomer = new Customer();
    temporaryCustomer.setTemporary(true);
    customerRepository.save(temporaryCustomer);

    Cart cart = new Cart();
    cart.setCustomer(temporaryCustomer);
    cart.setCartItem(new ArrayList<>());
    cart.setQuantity(0);
    cart.setFinalPrice(0.0);
    cart.setCoupon(null);

    cartRepository.save(cart);

    return CartMapper.toCartDTO(cart);
  }

  public CartDTO showCart(Integer cartId, Integer customerId) throws EshopException {
    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);

    if (cartOptional.isEmpty()) {
      throw new CartException(
          "Cart with ID: " + cartId + ", for customer ID: " + customerId + " doesn't exists!",
          HttpStatus.NOT_FOUND);
    }

    return CartMapper.toCartDTO(cartOptional.get());
  }

  public String addToCart(Integer customerId, Integer cartId, String itemId) throws EshopException {

    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);

    if (cartOptional.isEmpty()) {
      throw new CartException("Cart doesn't exists with ID: " + cartId, HttpStatus.NOT_FOUND);
    }

    Cart cart = cartOptional.get();

    Optional<Item> itemOptional = itemRepository.findById(itemId);

    if (itemOptional.isEmpty()) {
      throw new ItemException("Item doesn't exists with ID: " + itemId, HttpStatus.NOT_FOUND);
    }

    Item item = itemOptional.get();

    if (item.getStockQuantity() == 0) {
      throw new ItemException("Item with ID: " + itemId + " is sold out!", HttpStatus.BAD_REQUEST);
    }

    List<CartItem> cartItems = new ArrayList<>(cart.getCartItem());

    Optional<CartItem> cartItemOptional =
        cartItemRepository.findCartItemByIdAndCart_Id(itemId, cartId);

    if (cartItemOptional.isEmpty()) {
      CartItem newCartItem = new CartItem(item.getId(), item.getName(), 1, item.getPrice(), cart);
      cartItemRepository.save(newCartItem);
      cartItems.add(newCartItem);
    } else {
      CartItem existingCartItem = cartItemOptional.get();
      existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
      existingCartItem.setPrice(item.getPrice() * existingCartItem.getQuantity());
      cartItemRepository.save(existingCartItem);
    }

    int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    double totalPrice = cartItems.stream().mapToDouble(CartItem::getPrice).sum();

    cart.setCartItem(cartItems);
    cart.setQuantity(totalQuantity);
    cart.setOriginalPrice(totalPrice);
    cart.setFinalPrice(totalPrice);

    cartRepository.save(cart);

    return "Item add into cart successfully.";
  }

  public String removeItemFromCart(Integer customerId, Integer cartId, String itemId)
      throws EshopException {
    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);
    Optional<Item> itemOptional = itemRepository.findById(itemId);

    if (cartOptional.isEmpty()) {
      throw new CartException(
          "Cart doesn't exists with ID: " + cartId + " for customer ID: " + customerId + "!",
          HttpStatus.NOT_FOUND);
    }

    Cart cart = cartOptional.get();

    Optional<CartItem> cartItemOptional =
        cartItemRepository.findCartItemByIdAndCart_Id(itemId, cart.getId());

    if (cartItemOptional.isEmpty()) {
      throw new CartException(
          "Item doesn't exists in this cart: " + cartId + ", with this ID: " + itemId,
          HttpStatus.NOT_FOUND);
    }

    if (itemOptional.isEmpty()) {
      throw new ItemException("Item doesn't exists with this ID: " + itemId, HttpStatus.NOT_FOUND);
    }

    CartItem cartItem = cartItemOptional.get();
    Item item = itemOptional.get();

    if (cartItem.getQuantity() > 1) {
      cartItem.setQuantity(cartItem.getQuantity() - 1);
      cartItem.setPrice(item.getPrice() * cartItem.getQuantity());
      cartItemRepository.save(cartItem);
    } else {
      cart.getCartItem().remove(cartItem);
      cartItemRepository.delete(cartItem);
    }

    int totalQuantity = cart.getCartItem().stream().mapToInt(CartItem::getQuantity).sum();
    double totalPrice = cart.getCartItem().stream().mapToDouble(CartItem::getPrice).sum();

    cart.setQuantity(totalQuantity);
    cart.setOriginalPrice(totalPrice);
    cart.setFinalPrice(totalPrice);

    cartRepository.save(cart);

    return "Item removed successfully.";
  }

  public CartDTO recalculateCart(Integer customerId, Integer cartId) throws EshopException {
    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);
    double discount = 0;
    double totalPrice;

    if (cartOptional.isEmpty()) {
      throw new CartException(
          "Cart doesn't exists with ID: " + cartId + " for customer ID: " + customerId + "!",
          HttpStatus.NOT_FOUND);
    }

    Cart cart = cartOptional.get();

    if (cart.getCoupon() != null) {
      discount = cart.getOriginalPrice() * (cart.getCoupon().getDiscountInPercentage() / 100.0);
      totalPrice = cart.getOriginalPrice() - discount;
    } else {
      totalPrice = cart.getCartItem().stream().mapToDouble(CartItem::getPrice).sum();
    }

    int totalQuantity = cart.getCartItem().stream().mapToInt(CartItem::getQuantity).sum();

    cart.setQuantity(totalQuantity);
    cart.setFinalPrice(totalPrice);

    cartRepository.save(cart);

    return CartMapper.toCartDTO(cart);
  }

  public CartDTO addDiscountCoupon(Integer customerId, Integer cartId, String couponId)
      throws EshopException {
    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);
    Optional<Coupon> couponOptional = couponRepository.findById(couponId);
    double discount = 0;

    if (cartOptional.isEmpty()) {
      throw new CartException(
          "Cart doesn't exists with ID: " + cartId + " for customer ID: " + customerId + "!",
          HttpStatus.NOT_FOUND);
    }

    if (couponOptional.isEmpty()) {
      throw new CouponException("Coupon doesn't exists!", HttpStatus.NOT_FOUND);
    }

    Cart cart = cartOptional.get();
    Coupon newCoupon = couponOptional.get();
    Coupon oldCoupon = cart.getCoupon();

    cart.setCoupon(null);

    if (oldCoupon != null) {
      oldCoupon.getCart().remove(cart);
      couponRepository.save(oldCoupon);
    }

    cart.setCoupon(newCoupon);

    if (cart.getCoupon() != null) {
      discount = cart.getOriginalPrice() * (cart.getCoupon().getDiscountInPercentage() / 100.0);
    }

    double totalPriceWithDiscount = cart.getOriginalPrice() - discount;

    cart.setFinalPrice(totalPriceWithDiscount);

    cartRepository.save(cart);

    newCoupon.getCart().add(cart);
    couponRepository.save(newCoupon);

    return CartMapper.toCartDTO(cart);
  }

  public CartDTO removeDiscountCoupon(Integer customerId, Integer cartId) {
    Optional<Cart> cartOptional = cartRepository.findCartByIdAndCustomer_Id(cartId, customerId);

    if (cartOptional.isEmpty()) {
      throw new CartException(
          "Cart doesn't exists with ID: " + cartId + " for customer ID: " + customerId + "!",
          HttpStatus.NOT_FOUND);
    }

    Cart cart = cartOptional.get();

    if (cart.getCoupon() == null) {
      throw new CouponException("Coupon doesn't exists in this cart!", HttpStatus.NOT_FOUND);
    }

    Optional<Coupon> couponOptional = couponRepository.findById(cart.getCoupon().getId());

    if (couponOptional.isEmpty()) {
      throw new CouponException("Coupon doesn't exists!", HttpStatus.NOT_FOUND);
    }

    Coupon coupon = couponOptional.get();

    if (cart.getCoupon() == null) {
      throw new CartException("Cart hasn't any coupon!", HttpStatus.NOT_FOUND);
    }

    cart.setCoupon(null);

    double totalPrice = cart.getCartItem().stream().mapToDouble(CartItem::getPrice).sum();

    cart.setFinalPrice(totalPrice);

    cartRepository.save(cart);

    coupon.getCart().remove(cart);
    couponRepository.save(coupon);

    return CartMapper.toCartDTO(cart);
  }
}
