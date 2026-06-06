package com.javgr.clothingshop.dto;

import com.javgr.clothingshop.entity.Product;

import java.math.BigDecimal;

// Mot dong trong gio hang: san pham + so luong + thanh tien
public record CartLine(Product product, int quantity, BigDecimal subtotal) {
}
