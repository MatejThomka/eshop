package com.mth.eshop.controller;

import com.mth.eshop.model.DTO.*;
import com.mth.eshop.service.MainService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/")
public class MainController {

  private final MainService mainService;
  private final RestTemplate restTemplate;

  @Value("${cart.base.url}")
  private String cartBaseUrl;

  @Value("${coupon.base.url}")
  private String couponBaseUrl;

  public MainController(MainService mainService, RestTemplate restTemplate) {
    this.mainService = mainService;
    this.restTemplate = restTemplate;
  }

  @GetMapping("/{customerId}/{cartId}")
  public ResponseEntity<MainResponse> listAll(
      @PathVariable(required = false) Integer customerId,
      @PathVariable(required = false) Integer cartId) {

    List<ItemsDTO> itemList = mainService.getAllItems();
    List<CouponDTO> couponList = fetchCoupons();
    CartDTO cart;
    if (customerId == null && cartId == null) {
      cart = createCart();
    } else {
      cart = fetchCart(customerId, cartId);
    }

    MainResponse response = new MainResponse(itemList, cart, couponList);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/cart/{customerId}/{cartId}")
            .buildAndExpand(cart.customerId(), cart.id())
            .toUri();

    return ResponseEntity.ok().header("Location", location.toString()).body(response);
  }

  private List<CouponDTO> fetchCoupons() {
    return restTemplate
        .exchange(
            couponBaseUrl,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<List<CouponDTO>>() {})
        .getBody();
  }

  private CartDTO fetchCart(Integer customerId, Integer cartId) {
    String cartUrl = String.format("%s/%d/%d", cartBaseUrl, customerId, cartId);
    return restTemplate.getForObject(cartUrl, CartDTO.class);
  }

  private CartDTO createCart() {
    return restTemplate.getForObject(cartBaseUrl + "/create", CartDTO.class);
  }
}
