package com.bookstore.project.service.impl;

import com.bookstore.project.entity.User;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.enumerated.Role;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.service.PermissionService;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserService userService;

    @Override
    public void hasPermission(Permission permission) {

        // Lấy người dùng hiện tại từ SecurityContext
        User currentUser = userService.getCurrentUser();

        Permission[] permissions;
        switch (currentUser.getRole()) {
            case ADMIN:
                permissions = Role.ADMIN_PERMISSIONS;
                break;
            case CUSTOMER:
                permissions = Role.CUSTOMER_PERMISSIONS;
                break;
            default:
                throw RestException.forbidden();  // Nếu vai trò không hợp lệ
        }

        // Sử dụng Set thay vì List để cải thiện hiệu suất tìm kiếm
        Set<Permission> permissionSet = new HashSet<>(Arrays.asList(permissions));

        // Kiểm tra nếu người dùng không có quyền
        if (!permissionSet.contains(permission)) {
            throw RestException.forbidden();
        }
    }
}
