package com.mth.eshop.service;

import static com.mth.eshop.util.GlobalHelper.validateAccess;
import static com.mth.eshop.util.SecurityUtil.getCurrentUserEmail;
import static com.mth.eshop.util.UserHelper.*;

import com.mth.eshop.exception.CartException;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.exception.UserException;
import com.mth.eshop.model.Cart;
import com.mth.eshop.model.DTO.*;
import com.mth.eshop.model.User;
import com.mth.eshop.model.mapper.UserMapper;
import com.mth.eshop.repository.CartRepository;
import com.mth.eshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public UserDTO updateUser(UpdateUserRequest userRequestDTO) throws EshopException {
    validateAccess();
    User user = findUserByEmail(getCurrentUserEmail());

    if (userRequestDTO.firstname() != null) user.setFirstname(userRequestDTO.firstname());
    if (userRequestDTO.lastname() != null) user.setLastname(userRequestDTO.lastname());
    if (userRequestDTO.phone() != null) user.setPhone(userRequestDTO.phone());
    if (userRequestDTO.address() != null) user.setAddress(userRequestDTO.address());
    if (userRequestDTO.shippingAddress() != null)
      user.setShippingAddress(userRequestDTO.shippingAddress());

    return UserMapper.toCustomerDTO(userRepository.save(user));
  }

  public void changePassword(PasswordChangeRequest passwordChangeRequest) throws EshopException {
    validateAccess();
    User user = findUserByEmail(getCurrentUserEmail());

    if (!passwordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword())) {
      throw new UserException("Invalid old password", HttpStatus.BAD_REQUEST);
    }

    isPasswordValid(passwordChangeRequest.newPassword());
    isPasswordMatch(passwordChangeRequest.newPassword(), passwordChangeRequest.confirmPassword());

    user.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
    userRepository.save(user);
  }

  public void changeEmail(EmailChangeRequest emailChangeRequest) throws EshopException {
    validateAccess();

    isEmailMatch(getCurrentUserEmail(), emailChangeRequest.oldEmail());
    isEmailValid(emailChangeRequest.newEmail());
    isEmailMatch(emailChangeRequest.newEmail(), emailChangeRequest.confirmEmail());

    User user = findUserByEmail(getCurrentUserEmail());
    user.setEmail(emailChangeRequest.newEmail());
    userRepository.save(user);

    SecurityContextHolder.clearContext();
  }

  public void deleteUser() throws EshopException {
    validateAccess();

    User user = findUserByEmail(getCurrentUserEmail());

    userRepository.delete(user);
  }

  private User findUserById(Integer id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
  }

  private User findUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
  }

  private Cart findCart(Integer id) {
    return cartRepository
        .findById(id)
        .orElseThrow(() -> new CartException("Cart not found", HttpStatus.NOT_FOUND));
  }
}
