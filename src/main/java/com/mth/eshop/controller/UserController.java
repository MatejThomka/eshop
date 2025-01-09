package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.*;
import com.mth.eshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/details")
  public ResponseEntity<UserDTO> userDetails() {
    UserDTO user = userService.getUserDetails();
    return ResponseEntity.ok(user);
  }

  @PostMapping("/register/{userId}/{cartId}")
  public ResponseEntity<UserDTO> registerUser(
      @PathVariable Integer userId,
      @PathVariable Integer cartId,
      @Valid @RequestBody RegisterDTO registerDTO) {
    UserDTO user = userService.registerUser(userId, cartId, registerDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PatchMapping("/update")
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserRequest userDTO) {
    UserDTO updatedUser = userService.updateUser(userDTO);
    return ResponseEntity.ok(updatedUser);
  }

  @PatchMapping("/change-password")
  public ResponseEntity<String> changePassword(
      @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
    userService.changePassword(passwordChangeRequest);
    return ResponseEntity.ok("Password changed successfully");
  }

  @PatchMapping("/change-email")
  public ResponseEntity<String> changeEmail(
      @Valid @RequestBody EmailChangeRequest emailChangeRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    userService.changeEmail(emailChangeRequest, request, response);
    return ResponseEntity.ok("Email changed successfully, please login again");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser() {
    userService.deleteUser();
    return ResponseEntity.ok("User deleted successfully");
  }
}
