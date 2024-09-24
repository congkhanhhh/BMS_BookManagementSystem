package com.bookstore.project.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
}
