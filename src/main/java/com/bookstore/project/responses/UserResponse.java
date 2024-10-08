package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phoneNumber;
    private String profile;
}
