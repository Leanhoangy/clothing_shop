package com.javgr.clothingshop.controller.admin;

import com.javgr.clothingshop.dto.ProductFormDto;
import com.javgr.clothingshop.entity.Category;
import com.javgr.clothingshop.entity.Product;
import com.javgr.clothingshop.repository.CategoryRepository;
import com.javgr.clothingshop.repository.ProductRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Danh sách sản phẩm
    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAllWithDetails()
        );

        return "admin/products";
    }

    // Form thêm sản phẩm
    @GetMapping("/add")
    public String addForm(Model model) {

        ProductFormDto productForm = new ProductFormDto(
                null,
                "",
                "",
                "",
                null,
                0,
                "",
                null
        );

        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/product-form";
    }

    // Form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Integer id,
            Model model) {

        Product p = productRepository
                .findById(id)
                .orElseThrow();

        ProductFormDto dto = new ProductFormDto(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getThumbnail(),
                p.getCategory().getId()
        );

        model.addAttribute("productForm", dto);
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/product-form";
    }

    // Lưu thêm hoặc sửa
    @PostMapping("/save")
    public String save(
            @ModelAttribute ProductFormDto form) {

        Product product;

        if (form.id() != null) {
            product = productRepository
                    .findById(form.id())
                    .orElseThrow();
        } else {
            product = new Product();
        }

        Category category = categoryRepository
                .findById(form.categoryId())
                .orElseThrow();

        product.setName(form.name());
        product.setSlug(form.slug());
        product.setDescription(form.description());
        product.setPrice(form.price());
        product.setStock(form.stock());
        product.setThumbnail(form.thumbnail());
        product.setCategory(category);

        productRepository.save(product);

        return "redirect:/admin/products";
    }

    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id) {

        productRepository.deleteById(id);

        return "redirect:/admin/products";
    }
}