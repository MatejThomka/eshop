package com.mth.eshop.model.mapper;

import com.mth.eshop.model.Item;
import com.mth.eshop.model.record.ItemsDTO;

public class ItemsMapper {
    public static ItemsDTO toItemsDTO(Item item) {
        Integer numberOfReviews = item.getReview().size();

        Double roundedStars = Math.round(item.getStars() * 10.0) / 10.0;

        return new ItemsDTO(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getStockQuantity(),
                roundedStars,
                numberOfReviews);
    }
}
