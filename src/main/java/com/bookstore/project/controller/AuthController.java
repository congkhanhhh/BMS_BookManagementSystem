package com.bookstore.project.controller;

import com.bookstore.project.request.EmailPasswordCredentials;
import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.responses.AccessToken;
import com.bookstore.project.responses.UserContext;
import com.bookstore.project.responses.UserResponse;
import com.bookstore.project.service.TokenService;
import com.bookstore.project.service.UserService;
import com.bookstore.project.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Configuration
@RestController
@RequestMapping("/api/v1/public/authentication")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    @Operation(summary = "Get access token", description = "Authenticate user by using email/password credentials")
    public AccessToken authenticate(@Valid @RequestBody EmailPasswordCredentials credentials) {

        UserContext userContext = userService.authenticate(credentials.getEmail(), credentials.getPassword());
        String token = tokenService.generateToken(userContext);

        return new AccessToken(token);
    }
}
