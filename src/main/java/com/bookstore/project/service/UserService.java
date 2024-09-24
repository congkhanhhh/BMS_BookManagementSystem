package com.bookstore.project.service;

import com.bookstore.project.entity.User;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public UserResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnable(true);
        userRepository.save(user);
        return convertToUserResponseDTO(user);
    }

    // Đăng nhập người dùng
    public UserResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return convertToUserResponseDTO(user);
            }
        }
        return null; // hoặc throw exception nếu không muốn trả về null
    }

    private UserResponse convertToUserResponseDTO(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setPicture(user.getPicture());
        return userResponse;
    }

    // Quên mật khẩu
    public void forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Token có hiệu lực trong 1 giờ
            userRepository.save(user);

            // Gửi email với reset token
            sendResetPasswordEmail(user.getEmail(), token);
        }
    }

    // Phương thức gửi email chứa link reset mật khẩu
    private void sendResetPasswordEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/api/users/reset-password?token=" + token; // Thay đường dẫn bằng URL của bạn

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(mailMessage);
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Kiểm tra thời hạn của token
            if (user.getTokenExpiration().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu mới
                user.setResetToken(null); // Xóa token sau khi reset thành công
                user.setTokenExpiration(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // Lấy thông tin hồ sơ người dùng
    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return convertToUserProfileResponseDTO(userOptional.get());
        }
        return null; // hoặc throw exception nếu không tìm thấy người dùng
    }

    // Chỉnh sửa hồ sơ người dùng
    public UserProfileResponse editUserProfile(Long userId, UserProfileRequest userProfileRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userProfileRequest.getUsername());
            user.setEmail(userProfileRequest.getEmail());
            user.setAddress(userProfileRequest.getAddress());
            user.setPhoneNumber(userProfileRequest.getPhoneNumber());
            user.setPicture(userProfileRequest.getPicture());
            userRepository.save(user);
            return convertToUserProfileResponseDTO(user);
        }
        return null; // hoặc throw exception nếu không tìm thấy người dùng
    }

    // Chuyển đổi từ User entity sang UserProfileResponseDTO
    private UserProfileResponse convertToUserProfileResponseDTO(User user) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setId(user.getId());
        userProfileResponse.setUsername(user.getUsername());
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setAddress(user.getAddress());
        userProfileResponse.setPhoneNumber(user.getPhoneNumber());
        userProfileResponse.setPicture(user.getPicture());
        return userProfileResponse;
    }
    // Phương thức kiểm tra sự tồn tại của người dùng
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
