package com.bookstore.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
//    private final JwtRequestFilter jwtRequestFilter;
//
//    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
//        this.jwtRequestFilter = jwtRequestFilter;
//        this.userDetailsService = userDetailsService;
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())  // Tắt bảo vệ CSRF cho các endpoint API
            .authorizeHttpRequests(auth -> auth
                    // Cho phép truy cập Swagger mà không cần xác thực
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    // Cho phép truy cập công khai cho các API
                    .requestMatchers("/api/users/reset-password/**").permitAll()
                    .requestMatchers(
                            "/api/books/**",
                            "/api/genres/**",
                            "/api/users/**",
                            "/api/favorites/**",
                            "/api/orders/**",
                            "/api/profile/**"
                              // Sửa đây để cho phép toàn bộ path cho reset-password
                    ).permitAll()
                    // Xác thực tất cả các yêu cầu khác
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())  // Tắt form đăng nhập mặc định vì bạn sử dụng xác thực dựa trên API (ví dụ: JWT)
            .httpBasic(httpBasic -> httpBasic.disable());  // Tắt xác thực HTTP Basic

    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
