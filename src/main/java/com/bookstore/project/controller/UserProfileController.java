package com.bookstore.project.controller;

import com.bookstore.project.request.EditProfileRequest;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam Long userId) {
        UserProfileResponse response = userProfileService.getUserProfile(userId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{userId}")
    public ResponseEntity<UserProfileResponse> editUserProfile(@PathVariable Long userId, @RequestBody EditProfileRequest request) {
        UserProfileResponse updatedProfile = userProfileService.editUserProfile(userId, request);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
