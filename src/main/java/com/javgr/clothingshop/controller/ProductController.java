package com.javgr.clothingshop.controller;

import com.javgr.clothingshop.dto.ProductDto;
import com.javgr.clothingshop.entity.Product;
import com.javgr.clothingshop.entity.ProductImage;
import com.javgr.clothingshop.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductDto> all() {
        return productRepository.findAllWithDetails().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ProductDto> one(@PathVariable Integer id) {
        return productRepository.findByIdWithDetails(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getThumbnail(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getImages().stream().map(ProductImage::getImagePath).toList()
        );
    }
}
