package com.bookstore.project.service;

import com.bookstore.project.entity.User;
import com.bookstore.project.responses.UserContext;

public interface UserService{
    User getCurrentUser();

    UserContext authenticate(String email, String password);

    UserContext getUserContext(boolean required);

    User getUserByEmail(String email);
}
