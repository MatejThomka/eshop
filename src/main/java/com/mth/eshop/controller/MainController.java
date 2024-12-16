package com.mth.eshop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.Item;
import com.mth.eshop.model.record.CartDTO;
import com.mth.eshop.model.record.CouponDTO;
import com.mth.eshop.model.record.ItemDTO;
import com.mth.eshop.model.record.MainResponse;
import com.mth.eshop.service.MainService;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

  @GetMapping
  public ResponseEntity<?> listAll(
      @RequestParam(required = false) Integer customerId,
      @RequestParam(required = false) Integer cartId) {

    List<ItemDTO> itemList;
    List<CouponDTO> couponList;
    CartDTO cartDTO;

    try {
      itemList = mainService.getAllItems();

      couponList =
          restTemplate
              .exchange(
                  couponBaseUrl,
                  HttpMethod.GET,
                  HttpEntity.EMPTY,
                  new ParameterizedTypeReference<List<CouponDTO>>() {})
              .getBody();

      if (cartId != null && customerId != null) {
        String cartUrl =
            String.format("%s?customerId=%d&cartId=%d", cartBaseUrl, customerId, cartId);
        cartDTO = restTemplate.getForObject(cartUrl, CartDTO.class);
      } else {
        cartDTO = restTemplate.getForObject(cartBaseUrl + "/create", CartDTO.class);
      }
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    MainResponse response = new MainResponse(itemList, cartDTO, couponList);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addItem(@RequestBody Item item) {
    String message;

    try {
      message = mainService.addItem(item);
    } catch (EshopException e) {
      return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
