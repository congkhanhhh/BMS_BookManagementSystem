package com.bookstore.project.responses;

import com.bookstore.project.enumerated.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserContext implements UserDetails {

    private String username;

    private List<GrantedAuthority> authorities = new ArrayList<>();

    @Getter
    private Map<String, Object> data = new HashMap<>();

    public UserContext(String username, String... roles) {
        super();
        this.username = username;
        this.data = new HashMap<>();

        for (String role : roles) {
            this.authorities.add(Role.valueOf(role.toUpperCase()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
