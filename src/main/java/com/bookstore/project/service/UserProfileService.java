package com.bookstore.project.service;

import com.bookstore.project.dto.ChangepasswordDTO;
import com.bookstore.project.dto.UserProfileDTO;
import com.bookstore.project.entity.User;
import com.bookstore.project.entity.UserProfile;
import com.bookstore.project.repository.UserProfileRepository;
import com.bookstore.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository; // Thêm UserRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserProfileDTO createUserProfile(UserProfileDTO userProfileDTO) {
        Optional<User> user = userRepository.findById(Math.toIntExact(userProfileDTO.getUserId()));
        if (user.isPresent()) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user.get()); // Đặt đối tượng User đã tồn tại
            userProfile.setFullName(userProfileDTO.getFullName());
            userProfile.setAddress(userProfileDTO.getAddress());
            userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
            userProfile.setBirthdate(userProfileDTO.getBirthdate());
            UserProfile savedProfile = userProfileRepository.save(userProfile);
            return convertToDTO(savedProfile);
        } else {
            throw new RuntimeException("User not found with id: " + userProfileDTO.getUserId());
        }
    }

    public UserProfileDTO updateUserProfile(Long id, UserProfileDTO userProfileDTO) {
        Optional<UserProfile> existingProfile = userProfileRepository.findById(id);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            userProfile.setFullName(userProfileDTO.getFullName());
            userProfile.setAddress(userProfileDTO.getAddress());
            userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
            userProfile.setBirthdate(userProfileDTO.getBirthdate());
            UserProfile updatedProfile = userProfileRepository.save(userProfile);
            return convertToDTO(updatedProfile);
        } else {
            return null; // Handle this case as needed
        }
    }

    public UserProfileDTO getUserProfile(Long id) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        return userProfile.map(this::convertToDTO).orElse(null);
    }

    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }

    private UserProfileDTO convertToDTO(UserProfile userProfile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(userProfile.getId());
        dto.setUserId(userProfile.getUser().getId());
        dto.setFullName(userProfile.getFullName());
        dto.setAddress(userProfile.getAddress());
        dto.setPhoneNumber(userProfile.getPhoneNumber());
        dto.setBirthdate(userProfile.getBirthdate());
        return dto;
    }
}


