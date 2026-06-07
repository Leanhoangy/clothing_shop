package com.javgr.clothingshop.controller.admin;

import com.javgr.clothingshop.dto.ProductFormDto;
import com.javgr.clothingshop.entity.Category;
import com.javgr.clothingshop.entity.Product;
import com.javgr.clothingshop.entity.ProductImage;
import com.javgr.clothingshop.repository.CategoryRepository;
import com.javgr.clothingshop.repository.ProductImageRepository;
import com.javgr.clothingshop.repository.ProductRepository;
import java.nio.file.Path;
import java.util.UUID;import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductController(
            ProductImageRepository productImageRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productImageRepository = productImageRepository;
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
                null,
                0,
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

        Product product = productRepository
                .findByIdWithDetails(id)
                .orElseThrow();

        ProductFormDto dto = new ProductFormDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId()
        );

        model.addAttribute("productForm", dto);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/product-form";
    }
    
    // Lưu thêm hoặc sửa
    @PostMapping("/save")
    public String save(
            @ModelAttribute ProductFormDto form,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) throws IOException {

        // 1. GET or CREATE PRODUCT
        Product product;

        if (form.id() != null) {
            product = productRepository.findById(form.id())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        } else {
            product = new Product();
        }

        // 2. GET CATEGORY
        Category category = categoryRepository.findById(form.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3. SET PRODUCT INFO
        product.setName(form.name());
        product.setDescription(form.description());
        product.setPrice(form.price());
        product.setStock(form.stock());
        product.setCategory(category);

        // 4. SAVE PRODUCT FIRST (important for FK)
        product = productRepository.save(product);

        // 5. HANDLE FILE UPLOAD
        if (files != null && files.length > 0) {

            String folder = category.getSlug();

            //  IMPORTANT: store outside resources
            Path uploadDir = Paths.get("uploads/" + folder);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // optional: reset sort order if new images
            int sortOrder = productImageRepository.countByProduct(product) + 1;

            for (MultipartFile file : files) {

                if (file.isEmpty()) continue;

                // unique filename
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                Path filePath = uploadDir.resolve(fileName);

                // save file to disk
                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                // save DB record
                ProductImage image = new ProductImage();
                image.setProduct(product);
                image.setSortOrder(sortOrder++);

                // IMPORTANT: URL path (NOT file system path)
                image.setImagePath("/images/" + folder + "/" + fileName);

                productImageRepository.save(image);
            }
        }

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