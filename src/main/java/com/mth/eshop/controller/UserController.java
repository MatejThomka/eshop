package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.UserDTO;
import com.mth.eshop.model.DTO.LoginDTO;
import com.mth.eshop.model.DTO.RegisterDTO;
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

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> userDetails(@PathVariable Integer id) {
    UserDTO customer = userService.getUserDetails(id);
    return ResponseEntity.ok(customer);
  }

  @PostMapping("/register/{customerId}/{cartId}")
  public ResponseEntity<UserDTO> registerUser(
      @PathVariable Integer customerId,
      @PathVariable Integer cartId,
      @Valid @RequestBody RegisterDTO registerDTO) {
    UserDTO customer = userService.registerUser(customerId, cartId, registerDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(customer);
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
    UserDTO updatedCustomer = userService.updateUser(userDTO);
    return ResponseEntity.ok(updatedCustomer);
  }
}
