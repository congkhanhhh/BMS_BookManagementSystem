package com.bookstore.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileDTO {
    private int id;
    private int userId;
    private String fullName;
    private String address;
    private String phoneNumber;
    private LocalDate birthdate;
}

