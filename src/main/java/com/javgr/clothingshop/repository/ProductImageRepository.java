package com.javgr.clothingshop.repository;

import com.javgr.clothingshop.entity.Product;
import com.javgr.clothingshop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProductId(Integer productId);

    void deleteByProductId(Integer productId);
    int countByProduct(Product product);

}