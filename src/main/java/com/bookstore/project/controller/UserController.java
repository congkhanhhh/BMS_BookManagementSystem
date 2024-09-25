package com.bookstore.project.controller;

import com.bookstore.project.entity.User;
import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.responses.UserResponse;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.register(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    // Đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = userService.login(loginRequest);
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized nếu đăng nhập sai
        }
    }

    // Quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Reset token đã được gửi đến email của bạn.");
    }

    // Đặt lại mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean success = userService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Mật khẩu đã được thay đổi.");
        } else {
            return ResponseEntity.status(400).body("Token không hợp lệ hoặc đã hết hạn.");
        }
    }

    // Lấy thông tin hồ sơ người dùng
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfileResponse userProfile = userService.getUserProfile(id);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.status(404).body(null); // Not Found nếu không tìm thấy
        }
    }

    // Chỉnh sửa hồ sơ người dùng
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> editUserProfile(@PathVariable Long id, @RequestBody UserProfileRequest userProfileRequest) {
        UserProfileResponse updatedProfile = userService.editUserProfile(id, userProfileRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.status(404).body(null); // Not Found nếu không chỉnh sửa được
        }
    }
}
