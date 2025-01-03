package com.mth.eshop.service;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.UserException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Cart;
import com.mth.eshop.model.DTO.LoginDTO;
import com.mth.eshop.model.User;
import com.mth.eshop.model.DTO.UserDTO;
import com.mth.eshop.model.DTO.RegisterDTO;
import com.mth.eshop.model.mapper.UserMapper;
import com.mth.eshop.repository.CartRepository;
import com.mth.eshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mth.eshop.util.GlobalHelper.validateAccess;
import static com.mth.eshop.util.SecurityUtil.getCurrentUserEmail;
import static com.mth.eshop.util.SecurityUtil.hasRole;
import static com.mth.eshop.util.UserHelper.*;

@Service
public class UserService {

  UserRepository userRepository;
  CartRepository cartRepository;
  PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      CartRepository cartRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO getUserDetails() throws EshopException {
    String email = getCurrentUserEmail();
    validateAccess();

    User user = findUserByEmail(email);

    return UserMapper.toCustomerDTO(user);
  }

  public UserDTO registerUser(Integer customerId, Integer cartId, RegisterDTO registerDTO)
      throws EshopException {
    isEmailValid(registerDTO.email());
    isPasswordValid(registerDTO.password());
    isPhoneValid(registerDTO.phone());

    Cart cart = findCart(cartId);
    User registeringUser = findUserById(customerId);

    registeringUser.setTemporary(false);
    registeringUser.setEmail(registerDTO.email());
    registeringUser.setPassword(passwordEncoder.encode(registerDTO.password()));
    registeringUser.setFirstname(registerDTO.firstName());
    registeringUser.setLastname(registerDTO.lastName());
    registeringUser.setPhone(registerDTO.phone());
    registeringUser.setCart(cart);

    User savedUser = userRepository.save(registeringUser);

    return UserMapper.toCustomerDTO(savedUser);
  }

  public UserDTO loginUser(LoginDTO loginDTO) throws EshopException {
    User user = findUserByEmail(loginDTO.email());
    if (!passwordEncoder.matches(loginDTO.password(), user.getPassword()))
      throw new UserException("Incorrect credentials!", HttpStatus.BAD_REQUEST);

    return UserMapper.toCustomerDTO(user);
  }

  public UserDTO updateUser(UserDTO userDTO) throws EshopException {
    validateAccess();
    return null;
  }

  private User findUserById(Integer id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserException("Customer not found", HttpStatus.NOT_FOUND));
  }

  private User findUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserException("Customer not found", HttpStatus.NOT_FOUND));
  }

  private Cart findCart(Integer id) {
    return cartRepository
        .findById(id)
        .orElseThrow(() -> new CartException("Cart not found", HttpStatus.NOT_FOUND));
  }
}
