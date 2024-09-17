package com.bookstore.project.service;

import com.bookstore.project.dto.ChangepasswordDTO;
import com.bookstore.project.dto.UserDTO;
import com.bookstore.project.entity.User;
import com.bookstore.project.exception.ResourceNotFoundException;
import com.bookstore.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký người dùng mới
    public User signup(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Mã hóa password
        user.setRoleId(userDTO.getRoleId());
        user.setEnable(true); // Kích hoạt tài khoản
        user.setCreateAt(LocalDate.now());
        return userRepository.save(user);
    }

    // Xác thực người dùng khi login
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // Xác thực thành công
        }
        return null; // Login thất bại
    }

    public void changePassword(Long userId, ChangepasswordDTO changePasswordDTO) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }
}
