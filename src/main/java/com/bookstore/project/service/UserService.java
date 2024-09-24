package com.bookstore.project.service;

import com.bookstore.project.entity.User;
import com.bookstore.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }

    // Phương thức đăng ký người dùng
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Thay đổi mật khẩu
    public boolean changePassword(Long userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Cập nhật thông tin người dùng
    public User updateProfile(Long userId, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setAddress(updatedUser.getAddress());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            return userRepository.save(user);
        }
        return null;
    }
    // Phương thức để lấy tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Phương thức để xóa người dùng
    public boolean deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return true;
        }
        return false;
    }
}
