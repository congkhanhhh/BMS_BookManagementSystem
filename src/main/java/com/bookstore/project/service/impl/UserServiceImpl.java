package com.bookstore.project.service.impl;

import com.bookstore.project.entity.User;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.LoginRequest;
import com.bookstore.project.request.RegisterRequest;
import com.bookstore.project.request.UserProfileRequest;
import com.bookstore.project.responses.UserContext;
import com.bookstore.project.responses.UserProfileResponse;
import com.bookstore.project.responses.UserResponse;
import com.bookstore.project.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal(); // Trả về người dùng hiện tại
        } else {
            throw new AccessDeniedException("Unauthorized");
        }
    }
    @Override
    public UserContext authenticate(String email, String password) {
        User entity = getUserByEmail(email);
        if (!passwordEncoder.matches(password, entity.getPassword())) {
            log.info("User [{}] password is invalid", email);
            throw RestException.badRequest("Password is invalid");
        }

        UserContext userContext = new UserContext(entity.getEmail(), entity.getRole().name());
        userRepo.save(entity);

        return userContext;
    }

    @Override
    public UserContext getUserContext(boolean required) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof UserContext) {

            return (UserContext) authentication.getPrincipal();
        } else if (required) {
            throw RestException.unauthorized();
        }

        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userEntity = userRepo.findByEmail(email);
        if (userEntity.isEmpty()) {
            log.info("Email [{}] incorrect", email);
            throw RestException.notFound("Email or password is incorrect. Please check again.");
        }
        return userEntity.get();
    }
}
