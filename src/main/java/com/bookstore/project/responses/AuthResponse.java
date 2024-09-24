package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String jwt;
    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }
}
