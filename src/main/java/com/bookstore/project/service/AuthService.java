package com.bookstore.project.service;

import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.responses.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    UserResponse login(LoginRequest loginRequest);
    void forgotPassword(String email);
    boolean resetPassword(String token, String newPassword);
    UserProfileResponse getUserProfile(Long userId);
    UserProfileResponse editUserProfile(Long userId, UserProfileRequest userProfileRequest);
}
