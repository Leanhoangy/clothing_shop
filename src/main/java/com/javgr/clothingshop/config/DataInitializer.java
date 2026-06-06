package com.javgr.clothingshop.config;

import com.javgr.clothingshop.entity.User;
import com.javgr.clothingshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // Tao san 2 tai khoan demo neu DB chua co
    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            createOrUpdateDefaultUser(userRepository, "admin@huyrc.vn", "Quản trị viên", "admin123", "ADMIN");
            createOrUpdateDefaultUser(userRepository, "khach@huyrc.vn", "Khách Demo", "123456", "CUSTOMER");
        };
    }

    private void createOrUpdateDefaultUser(UserRepository repo,
                                           String email, String name, String rawPassword, String role) {
        repo.findByEmail(email).ifPresentOrElse(user -> {
            user.setFullName(name);
            user.setPassword(rawPassword);
            user.setRole(role);
            user.setEnabled(true);
            repo.save(user);
        }, () -> {
            User u = new User();
            u.setEmail(email);
            u.setFullName(name);
            u.setPassword(rawPassword);
            u.setRole(role);
            u.setEnabled(true);
            repo.save(u);
        });
    }
}
