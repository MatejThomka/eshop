package com.mth.eshop.service;

import com.mth.eshop.exception.EshopException;
import com.mth.eshop.model.DTO.ItemsDTO;
import com.mth.eshop.model.mapper.ItemsMapper;
import com.mth.eshop.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MainService {

  private final ItemRepository itemRepository;

  public MainService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  public List<ItemsDTO> getAllItems() throws EshopException {
    return Optional.of(itemRepository.count())
        .filter(count -> count > 0)
        .map(count -> itemRepository.findAll().stream().map(ItemsMapper::toItemsDTO).toList())
        .orElse(List.of());
  }
}
