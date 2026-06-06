package com.javgr.clothingshop.controller;

import com.javgr.clothingshop.repository.CartItemRepository;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Bo sung bien dung chung cho moi trang (header) -> so luong trong gio hang
@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository userRepository;
    private final CartItemRepository cartRepository;

    public GlobalModelAttributes(UserRepository userRepository, CartItemRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @ModelAttribute("cartCount")
    public long cartCount(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return 0;
        }
        return userRepository.findByEmail(auth.getName())
                .map(u -> cartRepository.countByUserId(u.getId()))
                .orElse(0L);
    }
}
