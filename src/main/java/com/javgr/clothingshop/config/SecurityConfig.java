package com.javgr.clothingshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Ma hoa mat khau bang BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Trang cong khai: ai cung xem duoc
                        .requestMatchers("/", "/products/**", "/api/**",
                                "/css/**", "/images/**", "/js/**",
                                "/login", "/register").permitAll()
                        // Khu vuc can dang nhap (gio hang, don hang, tai khoan, thanh toan)
                        .requestMatchers("/account/**", "/cart/**", "/orders/**", "/checkout/**").authenticated()
                        // Khu vuc quan tri
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")              // trang login tu thiet ke
                        .loginProcessingUrl("/login")     // form POST gui ve day
                        .usernameParameter("email")       // dang nhap bang email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );
        return http.build();
    }
}
