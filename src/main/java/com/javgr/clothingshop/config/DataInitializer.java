package com.javgr.clothingshop.config;

import com.javgr.clothingshop.entity.User;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // Tao san 2 tai khoan demo neu DB chua co (mat khau ma hoa BCrypt).
    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            createIfMissing(userRepository, encoder, "admin@huyrc.vn", "Quản trị viên", "admin123", "ADMIN");
            createIfMissing(userRepository, encoder, "khach@huyrc.vn", "Khách Demo", "123456", "CUSTOMER");
        };
    }

    private void createIfMissing(UserRepository repo, PasswordEncoder encoder,
                                 String email, String name, String rawPassword, String role) {
        if (repo.existsByEmail(email)) {
            return;
        }
        User u = new User();
        u.setEmail(email);
        u.setFullName(name);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(role);
        u.setEnabled(true);
        repo.save(u);
    }
}
