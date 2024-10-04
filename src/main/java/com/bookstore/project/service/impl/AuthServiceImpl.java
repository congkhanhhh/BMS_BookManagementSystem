package com.bookstore.project.service.impl;

import com.bookstore.project.config.AppConfig;
import com.bookstore.project.entity.User;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.responses.UserResponse;
import com.bookstore.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppConfig appConfig;

    public UserResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnable(true);
        userRepo.save(user);
        return convertToUserResponseDTO(user);
    }

    // Đăng nhập người dùng
    public UserResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepo.findByEmail(loginRequest.getEmail());
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
        return userResponse;
    }

    // Quên mật khẩu
    public void forgotPassword(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Token có hiệu lực trong 1 giờ
            userRepo.save(user);

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
        Optional<User> userOptional = userRepo.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Kiểm tra thời hạn của token
            if (user.getTokenExpiration().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu mới
                user.setResetToken(null); // Xóa token sau khi reset thành công
                user.setTokenExpiration(null);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }

    // Lấy thông tin hồ sơ người dùng
    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            return convertToUserProfileResponseDTO(userOptional.get());
        }
        return null;
    }

    // Chỉnh sửa hồ sơ người dùng
    public UserProfileResponse editUserProfile(Long userId, UserProfileRequest userProfileRequest) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userProfileRequest.getUsername());
            user.setEmail(userProfileRequest.getEmail());
            user.setAddress(userProfileRequest.getAddress());
            user.setPhoneNumber(userProfileRequest.getPhoneNumber());
            userRepo.save(user);
            return convertToUserProfileResponseDTO(user);
        }
        return null;
    }


    // Chuyển đổi từ User entity sang UserProfileResponseDTO
    private UserProfileResponse convertToUserProfileResponseDTO(User user) {
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setId(user.getId());
        userProfileResponse.setUsername(user.getUsername());
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setAddress(user.getAddress());
        userProfileResponse.setPhoneNumber(user.getPhoneNumber());
        return userProfileResponse;
    }
}
