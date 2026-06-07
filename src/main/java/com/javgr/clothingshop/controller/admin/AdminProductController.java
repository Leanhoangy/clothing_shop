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

        Product product = productRepository
                .findByIdWithDetails(id)
                .orElseThrow();

        ProductFormDto dto = new ProductFormDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getThumbnail(),
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
            @RequestParam(value = "files", required = false)
            MultipartFile[] files
    ) throws IOException {

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

        product = productRepository.save(product);

        // Upload ảnh
        if (files != null && files.length > 0) {

            String folder = category.getSlug();

            Path uploadDir = Paths.get(
                    "src/main/resources/static/images/" + folder
            );

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            int sortOrder = 1;

            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    continue;
                }

                String fileName =
                        UUID.randomUUID()
                        + "_"
                        + file.getOriginalFilename();

                Path filePath = uploadDir.resolve(fileName);

                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                ProductImage image = new ProductImage();

                image.setProduct(product);
                image.setImagePath(
                        "/images/" + folder + "/" + fileName
                );
                image.setSortOrder(sortOrder++);

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