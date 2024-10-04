package com.bookstore.project.responses;

import com.bookstore.project.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phoneNumber;
    private String message; // Thêm thông điệp phản hồi nếu cần

}
