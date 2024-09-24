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
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection for API endpoints
                .authorizeHttpRequests(auth -> auth
                        // Allow Swagger access without authentication
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html" ).permitAll()
                        // Allow public access to signup and login APIs
                        .requestMatchers(   "/api/books/**","/api/genres/**","/api/users/**",
                                "/api/favorites/**","/api/orders/**","/api/profile/**","/api/users/reset-password?token=").permitAll()
                        // Authenticate all other requests
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())  // Disable the default login form since you are using API-based authentication (e.g., JWT)
                .httpBasic(httpBasic -> httpBasic.disable());  // Disable HTTP Basic Authentication

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
