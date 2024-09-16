package com.bookstore.project.controller;

import com.bookstore.project.dto.UserDTO;
import com.bookstore.project.entity.User;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        User newUser = userService.signup(userDTO);
        if (newUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register user.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);
        if (user != null) {
            return ResponseEntity.ok("Login successful.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }
}
