package com.javgr.clothingshop.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        Integer id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        Integer stock,
        String thumbnail,
        String category,
        List<String> images
) {
}
