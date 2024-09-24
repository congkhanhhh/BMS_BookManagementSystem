package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserProfileRequest {
    private String username;
    private String email;
    private String address;
    private String phoneNumber;
    private String picture;
}
