package com.bookstore.project.controller;

import com.bookstore.project.entity.User;
import com.bookstore.project.responses.UserResponse;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy tất cả người dùng
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setAddress(user.getAddress());
                    response.setPhoneNumber(user.getPhoneNumber());
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    // Xóa người dùng
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found!");
        }
    }
}
