package com.bookstore.project.service;

import com.bookstore.project.responses.UserContext;

public interface TokenService {

    String generateToken(UserContext userContext);

    UserContext parseToken(String token);

    String generateDownloadToken(Long id, String type);

    boolean verifyDownloadToken(Long id, String type, String token);

}