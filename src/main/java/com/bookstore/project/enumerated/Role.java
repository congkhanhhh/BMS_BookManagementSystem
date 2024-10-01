package com.bookstore.project.enumerated;

import com.bookstore.project.exception.RestException;
import org.springframework.security.core.GrantedAuthority;



public enum Role implements GrantedAuthority {
    ADMIN, CUSTOMER;

    public static final Permission[] ADMIN_PERMISSIONS = new Permission[]{
            Permission.ADMIN_VIEW_BOOK,
            Permission.ADMIN_ADD_EDIT_BOOK
    };

    public static final Permission[] CUSTOMER_PERMISSIONS = new Permission[]{
            Permission.CUSTOMER_VIEW_BOOK
    };

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name().toUpperCase();
    }

    public static Role fromStringThrowEx(String str) {
        try {
            return Role.valueOf(str.toUpperCase());
        } catch (Exception e) {
            throw RestException.internalServerError();
        }
    }
}
