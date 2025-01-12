package com.mth.eshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.CouponException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.model.*;
import com.mth.eshop.model.DTO.CartDTO;
import com.mth.eshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

class CartServiceTest {

  @Mock
  private CartRepository cartRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private CartService cartService;

  private User user;
  private Cart cart;
  private Item item;
  private Coupon coupon;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1);
    user.setTemporary(true);
    user.setRole(Role.USER);

    cart = new Cart();
    cart.setId(1);
    cart.setUser(user);
    cart.setCartItem(new ArrayList<>());
    cart.setQuantity(0);
    cart.setFinalPrice(0.0);

    item = new Item();
    item.setId("item1");
    item.setName("Item 1");
    item.setPrice(100.0);
    item.setStockQuantity(10);

    coupon = new Coupon();
    coupon.setId("coupon1");
    coupon.setCart(new ArrayList<>());
  }

  @Test
  void testCreateCart() {
    // Arrange
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
      Cart c = invocation.getArgument(0);
      c.setId(1);
      return c;
    });

    // Act
    CartDTO result = cartService.createCart();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.id());
    verify(userRepository, times(1)).save(any(User.class));
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  void testShowCartSuccess() throws Exception {
    // Arrange
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));

    // Act
    CartDTO result = cartService.showCart(1, 1);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.id());
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
  }

  @Test
  void testShowCartNotFound() {
    // Arrange
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(CartException.class, () -> cartService.showCart(1, 1));
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
  }

  @Test
  void testAddToCartSuccess() throws Exception {
    // Arrange
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));
    when(itemRepository.findById("item1")).thenReturn(Optional.of(item));
    when(cartItemRepository.findCartItemByIdAndCart_Id("item1", 1)).thenReturn(Optional.empty());

    // Act
    CartDTO result = cartService.addToCart(1, 1, "item1");

    // Assert
    assertNotNull(result);
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
    verify(itemRepository, times(1)).findById("item1");
    verify(cartItemRepository, times(1)).save(any(CartItem.class));
  }

  @Test
  void testAddToCartItemSoldOut() {
    // Arrange
    item.setStockQuantity(0);
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));
    when(itemRepository.findById("item1")).thenReturn(Optional.of(item));

    // Act & Assert
    assertThrows(ItemException.class, () -> cartService.addToCart(1, 1, "item1"));
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
    verify(itemRepository, times(1)).findById("item1");
  }

  @Test
  void testRemoveDiscountCouponSuccess() throws Exception {
    // Arrange
    coupon.getCart().add(cart);
    cart.setCoupon(coupon);
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));

    // Act
    CartDTO result = cartService.removeDiscountCoupon(1, 1);

    // Assert
    assertNotNull(result);
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
    verify(cartRepository, times(1)).save(cart);
    verify(couponRepository, times(1)).save(coupon);
  }

  @Test
  void testRemoveDiscountCouponNotFound() {
    // Arrange
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));

    // Act & Assert
    assertThrows(CouponException.class, () -> cartService.removeDiscountCoupon(1, 1));
    verify(cartRepository, times(1)).findCartByIdAndUser_Id(1, 1);
  }
}
