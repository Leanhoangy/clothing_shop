package com.javgr.clothingshop.repository;

import com.javgr.clothingshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.images " +
           "LEFT JOIN FETCH p.category " +
           "ORDER BY p.id")
    List<Product> findAllWithDetails();

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.images " +
           "LEFT JOIN FETCH p.category " +
           "WHERE p.category.id = :categoryId " +
           "ORDER BY p.id")
    List<Product> findByCategoryIdWithDetails(@Param("categoryId") Integer categoryId);

    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.images " +
           "LEFT JOIN FETCH p.category " +
           "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(Integer id);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.images " +
           "LEFT JOIN FETCH p.category " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "ORDER BY p.id")
    List<Product> searchByName(@Param("q") String q);
}
