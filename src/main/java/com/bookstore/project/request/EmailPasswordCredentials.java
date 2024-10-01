package com.bookstore.project.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailPasswordCredentials implements Serializable {

    @Email
    @NotEmpty(message = "Required!")
    @Size(min = 4, max = 64)
    private String email;

    @NotEmpty(message = "Required!")
    @Size(min = 8, max = 64)
    private String password;

}
