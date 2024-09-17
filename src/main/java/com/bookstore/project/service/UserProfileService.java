package com.bookstore.project.service;

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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserProfileDTO createUserProfile(UserProfileDTO userProfileDTO) {
        Optional<User> user = userRepository.findById(userProfileDTO.getUserId());
        if (user.isPresent()) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user.get());
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

    public UserProfileDTO updateUserProfile(long userId, UserProfileDTO userProfileDTO) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            userProfile.setFullName(userProfileDTO.getFullName());
            userProfile.setAddress(userProfileDTO.getAddress());
            userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
            userProfile.setBirthdate(userProfileDTO.getBirthdate());
            UserProfile updatedProfile = userProfileRepository.save(userProfile);
            return convertToDTO(updatedProfile);
        } else {
            throw new RuntimeException("User profile not found for user id: " + userId);
        }
    }

    public UserProfileDTO getUserProfile(long userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
        return userProfile.map(this::convertToDTO).orElse(null);
    }

    public void deleteUserProfile(long userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile.isPresent()) {
            userProfileRepository.delete(userProfile.get());
        } else {
            throw new RuntimeException("User profile not found for user id: " + userId);
        }
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
