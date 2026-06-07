package com.javgr.clothingshop.controller;

import com.javgr.clothingshop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShopController {

    private final ProductRepository productRepository;

    public ShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Trang chu: danh sach san pham
    @GetMapping("/")
    @Transactional(readOnly = true)
    public String index(Model model) {
        model.addAttribute("products", productRepository.findAllWithDetails());
        return "index";
    }

    // Trang chi tiet 1 san pham
    @GetMapping("/products/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        return productRepository.findByIdWithDetails(id)
                .map(p -> {
                    model.addAttribute("product", p);
                    return "product";
                })
                .orElse("redirect:/");
    }
}
