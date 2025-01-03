package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.LoginDTO;
import com.mth.eshop.model.DTO.RegisterDTO;
import com.mth.eshop.model.DTO.UserDTO;
import com.mth.eshop.service.UserService;
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

  @PostMapping("/login")
  public ResponseEntity<UserDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
    UserDTO user = userService.loginUser(loginDTO);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logoutUser() {
    return ResponseEntity.ok("Logout successful");
  }

  @PatchMapping("/update")
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
    UserDTO updatedUser = userService.updateUser(userDTO);
    return ResponseEntity.ok(updatedUser);
  }
}
