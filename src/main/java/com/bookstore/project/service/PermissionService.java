package com.bookstore.project.service;

import com.bookstore.project.enumerated.Permission;

public interface PermissionService {
    void hasPermission(Permission permission);
}
