package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String email;
    private String address;
    private String phoneNumber;
    private String picture;
}
