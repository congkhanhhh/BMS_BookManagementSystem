package com.bookstore.project.service.impl;

import com.bookstore.project.entity.User;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.responses.UserContext;
import com.bookstore.project.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        UserContext ctx = getUserContext(true);
        Optional<User> user = userRepo.findByEmail(ctx.getUsername());
        if (user.isEmpty()) {
            throw RestException.notFound();
        }
        return user.get();
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
