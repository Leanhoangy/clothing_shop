package com.javgr.clothingshop.controller;

import com.javgr.clothingshop.dto.CartLine;
import com.javgr.clothingshop.entity.*;
import com.javgr.clothingshop.repository.CartItemRepository;
import com.javgr.clothingshop.repository.OrderRepository;
import com.javgr.clothingshop.repository.ProductRepository;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CartController(CartItemRepository cartRepository, ProductRepository productRepository,
                          OrderRepository orderRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // ----- Them san pham vao gio -----
    @PostMapping("/add")
    @Transactional
    public String add(@RequestParam Integer productId,
                      @RequestParam(defaultValue = "1") Integer quantity,
                      Authentication auth) {
        User u = currentUser(auth);
        CartItem item = cartRepository.findByUserIdAndProductId(u.getId(), productId)
                .orElseGet(() -> {
                    CartItem c = new CartItem();
                    c.setUserId(u.getId());
                    c.setProductId(productId);
                    c.setQuantity(0);
                    return c;
                });
        item.setQuantity(item.getQuantity() + Math.max(1, quantity));
        cartRepository.save(item);
        return "redirect:/cart";
    }

    // ----- Xem gio hang -----
    @GetMapping
    @Transactional(readOnly = true)
    public String viewCart(Authentication auth, Model model) {
        User u = currentUser(auth);
        List<CartLine> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cartRepository.findByUserId(u.getId())) {
            Product p = productRepository.findById(ci.getProductId()).orElse(null);
            if (p == null) continue;
            BigDecimal sub = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            lines.add(new CartLine(p, ci.getQuantity(), sub));
            total = total.add(sub);
        }

        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        model.addAttribute("user", u);
        return "cart";
    }

    // ----- Doi so luong -----
    @PostMapping("/update")
    @Transactional
    public String update(@RequestParam Integer productId, @RequestParam Integer quantity, Authentication auth) {
        User u = currentUser(auth);
        cartRepository.findByUserIdAndProductId(u.getId(), productId).ifPresent(ci -> {
            if (quantity <= 0) cartRepository.delete(ci);
            else { ci.setQuantity(quantity); cartRepository.save(ci); }
        });
        return "redirect:/cart";
    }

    // ----- Xoa 1 san pham khoi gio -----
    @PostMapping("/remove")
    @Transactional
    public String remove(@RequestParam Integer productId, Authentication auth) {
        User u = currentUser(auth);
        cartRepository.findByUserIdAndProductId(u.getId(), productId).ifPresent(cartRepository::delete);
        return "redirect:/cart";
    }

    // ----- Dat hang: tao Order tu gio, xoa gio -----
    @PostMapping("/checkout")
    @Transactional
    public String checkout(@RequestParam String recipientName,
                           @RequestParam String phone,
                           @RequestParam String address,
                           Authentication auth, RedirectAttributes ra) {
        User u = currentUser(auth);
        List<CartItem> items = cartRepository.findByUserId(u.getId());
        if (items.isEmpty()) {
            ra.addFlashAttribute("err", "Giỏ hàng đang trống.");
            return "redirect:/cart";
        }

        Order order = new Order();
        order.setUserId(u.getId());
        order.setRecipientName(recipientName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : items) {
            Product p = productRepository.findById(ci.getProductId()).orElse(null);
            if (p == null) continue;
            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setProductName(p.getName());   // snapshot ten + gia luc dat
            oi.setPrice(p.getPrice());
            oi.setQuantity(ci.getQuantity());
            order.addItem(oi);
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        order.setTotalAmount(total);

        orderRepository.save(order);          // cascade luu luon order_items
        cartRepository.deleteByUserId(u.getId());
        return "redirect:/orders/" + order.getId();
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Khong tim thay tai khoan dang nhap"));
    }
}
