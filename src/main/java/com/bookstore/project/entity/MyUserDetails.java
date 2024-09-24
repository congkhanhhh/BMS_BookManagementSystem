package com.bookstore.project.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Bạn có thể trả về các quyền của người dùng tại đây, nếu có
        return null; // Thay đổi cho phù hợp với ứng dụng của bạn
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Có thể thay đổi theo logic của bạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Có thể thay đổi theo logic của bạn
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Có thể thay đổi theo logic của bạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Có thể thay đổi theo logic của bạn
    }

    public User getUser() {
        return user;
    }
}

