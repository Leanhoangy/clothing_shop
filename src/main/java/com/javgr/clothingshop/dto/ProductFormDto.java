package com.javgr.clothingshop.dto;

import java.math.BigDecimal;

public record ProductFormDto(
        Integer id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        Integer stock,
        String thumbnail,
        Integer categoryId
) {
}