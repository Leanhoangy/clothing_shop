package com.javgr.clothingshop.controller.user;

import com.javgr.clothingshop.entity.User;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Trang thong tin ca nhan
    @GetMapping
    @Transactional(readOnly = true)
    public String profile(Authentication auth, Model model) {
        model.addAttribute("user", currentUser(auth));
        return "user/profile";
    }

    // Cap nhat ho ten + so dien thoai
    @PostMapping("/update")
    @Transactional
    public String update(@RequestParam String fullName,
                         @RequestParam(required = false) String phone,
                         Authentication auth, RedirectAttributes ra) {
        User u = currentUser(auth);
        if (fullName != null && !fullName.isBlank()) {
            u.setFullName(fullName.trim());
        }
        u.setPhone(phone);
        userRepository.save(u);
        ra.addFlashAttribute("updated", true);
        return "redirect:/account";
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Khong tim thay tai khoan dang nhap"));
    }
}
