package com.javgr.clothingshop.repository;

import com.javgr.clothingshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}