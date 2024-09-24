package com.bookstore.project.service;

import com.bookstore.project.entity.User;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.EditProfileRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User u = user.get();
            UserProfileResponse response = new UserProfileResponse();
            response.setId(u.getId());
            response.setUsername(u.getUsername());
            response.setEmail(u.getEmail());
            response.setAddress(u.getAddress());
            response.setPhoneNumber(u.getPhoneNumber());
            response.setPicture(u.getPicture());
            return response;
        }
        return null;
    }

    public UserProfileResponse editUserProfile(Long userId, EditProfileRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Cập nhật thông tin người dùng
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPicture(request.getPicture());

            userRepository.save(user); // Lưu thông tin đã cập nhật

            // Trả về thông tin đã chỉnh sửa dưới dạng response
            UserProfileResponse response = new UserProfileResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setAddress(user.getAddress());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setPicture(user.getPicture());
            return response;
        }
        return null;
    }
}

