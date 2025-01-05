package com.mth.eshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.ItemException;
import com.mth.eshop.model.*;
import com.mth.eshop.model.DTO.CartDTO;
import com.mth.eshop.repository.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CartServiceTest {

  @Mock private CartRepository cartRepository;
  @Mock private ItemRepository itemRepository;
  @Mock private CartItemRepository cartItemRepository;
  @Mock private UserRepository userRepository;
  @Mock private CouponRepository couponRepository;

  private CartService cartService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cartService =
        new CartService(
            cartRepository,
            itemRepository,
            cartItemRepository,
                userRepository,
            couponRepository);
  }

  @Test
  void createCart_createsCartSuccessfully() {
    // Arrange
    User temporaryUser = new User();
    temporaryUser.setTemporary(true);
    when(userRepository.save(any(User.class))).thenReturn(temporaryUser);

    Cart cart = new Cart();
    cart.setUser(temporaryUser);
    cart.setQuantity(0);
    cart.setFinalPrice(0.0);
    when(cartRepository.save(any(Cart.class))).thenReturn(cart);

    // Act
    CartDTO result = cartService.createCart();

    // Assert
    assertNotNull(result);
    assertEquals(0, result.quantity());
    assertEquals(0.0, result.finalPrice());
    verify(userRepository).save(any(User.class));
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void showCart_returnsCartSuccessfully() throws EshopException {
    // Arrange
    Cart cart = new Cart();
    cart.setId(1);
    User user = new User();
    user.setId(1);
    cart.setUser(user);
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));

    // Act
    CartDTO result = cartService.showCart(1, 1);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.id());
    verify(cartRepository).findCartByIdAndUser_Id(1, 1);
  }

  @Test
  void showCart_throwsExceptionWhenCartNotFound() {
    // Arrange
    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.empty());

    // Act & Assert
    CartException exception = assertThrows(CartException.class, () -> cartService.showCart(1, 1));
    assertEquals("Cart ID: 1 with customer ID: 1 doesn't exist!", exception.getMessage());
    verify(cartRepository).findCartByIdAndUser_Id(1, 1);
  }

  @Test
  void addToCart_addsNewItemSuccessfully() throws EshopException {
    // Arrange
    Cart cart = new Cart();
    cart.setId(1);
    User user = new User();
    user.setId(1);
    cart.setUser(user);

    Item item = new Item();
    item.setId("item1");
    item.setStockQuantity(10);
    item.setPrice(100.0);

    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));
    when(itemRepository.findById("item1")).thenReturn(Optional.of(item));
    when(cartItemRepository.findCartItemByIdAndCart_Id("item1", 1)).thenReturn(Optional.empty());
    when(cartRepository.save(any(Cart.class))).thenReturn(cart);

    // Act
    CartDTO result = cartService.addToCart(1, 1, "item1");

    // Assert
    assertNotNull(result);
    verify(cartRepository).findCartByIdAndUser_Id(1, 1);
    verify(itemRepository).findById("item1");
    verify(cartItemRepository).findCartItemByIdAndCart_Id("item1", 1);
    verify(cartItemRepository).save(any(CartItem.class));
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void addToCart_throwsExceptionWhenItemSoldOut() {
    // Arrange
    Cart cart = new Cart();
    cart.setId(1);
    User user = new User();
    user.setId(1);
    cart.setUser(user);

    Item item = new Item();
    item.setId("item1");
    item.setStockQuantity(0);

    when(cartRepository.findCartByIdAndUser_Id(1, 1)).thenReturn(Optional.of(cart));
    when(itemRepository.findById("item1")).thenReturn(Optional.of(item));

    // Act & Assert
    ItemException exception =
        assertThrows(ItemException.class, () -> cartService.addToCart(1, 1, "item1"));
    assertEquals("Item with ID: item1 is sold out!", exception.getMessage());
    verify(itemRepository).findById("item1");
  }
}
