package com.bookstore.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final String API_KEY = "";

    @GetMapping("/api/test")
    public ResponseEntity<String> test(@RequestHeader("Authorization") String apiKey) {
        if (API_KEY.equals(apiKey)) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }
    }
}
