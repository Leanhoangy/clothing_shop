package com.javgr.clothingshop.controller;

import com.javgr.clothingshop.entity.Order;
import com.javgr.clothingshop.entity.User;
import com.javgr.clothingshop.repository.OrderRepository;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Lich su don hang cua user
    @GetMapping
    @Transactional(readOnly = true)
    public String list(Authentication auth, Model model) {
        User u = currentUser(auth);
        model.addAttribute("orders", orderRepository.findByUserIdOrderByIdDesc(u.getId()));
        return "orders";
    }

    // Chi tiet / xac nhan 1 don hang
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable Integer id, Authentication auth, Model model) {
        User u = currentUser(auth);
        Order order = orderRepository.findByIdAndUserId(id, u.getId()).orElse(null);
        if (order == null) {
            return "redirect:/orders";
        }
        order.getItems().size();   // nap danh sach item trong transaction
        model.addAttribute("order", order);
        return "order-detail";
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Khong tim thay tai khoan dang nhap"));
    }
}
