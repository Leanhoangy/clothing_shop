package com.javgr.clothingshop.dto;

import com.javgr.clothingshop.entity.Category;

import java.util.List;

// Mot nhom cha + cac danh muc con (dung cho menu phan cap)
public record CategoryGroup(Category parent, List<Category> children) {
}
