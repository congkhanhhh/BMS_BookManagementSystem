package com.bookstore.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangepasswordDTO {
    private String oldPassword;
    private String newPassword;
}
