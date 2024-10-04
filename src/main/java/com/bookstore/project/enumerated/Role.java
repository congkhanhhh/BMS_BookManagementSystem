package com.bookstore.project.enumerated;

import com.bookstore.project.exception.RestException;
import org.springframework.security.core.GrantedAuthority;



public enum Role implements GrantedAuthority {
    ADMIN, CUSTOMER;

    public static final Permission[] ADMIN_PERMISSIONS = new Permission[]{
            // Book management
            Permission.VIEW_ALL_BOOK,        // Xem tất cả sách
            Permission.ADD_BOOK,             // Thêm sách mới
            Permission.EDIT_BOOK,            // Chỉnh sửa sách
            Permission.DELETE_BOOK,          // Xóa sách
            Permission.UPLOAD_IMAGE_BOOK,    // Tải ảnh lên cho sách

            // Order management
            Permission.VIEW_ORDER,           // Xem tất cả đơn hàng
            Permission.VIEW_ORDER_DETAILS,   // Xem chi tiết đơn hàng
            Permission.CREATE_ORDER,         // Tạo đơn hàng cho khách
            Permission.EDIT_ORDER,           // Chỉnh sửa đơn hàng
            Permission.DELETE_ORDER,         // Xóa đơn hàng
            Permission.SEARCH_ORDER,         // Tìm kiếm đơn hàng
            Permission.VIEW_OWN_ORDERS,

            // Favorite book management
            Permission.VIEW_USER_FAVORITE_BOOK,   // Xem danh sách yêu thích của user
            Permission.ADD_FAVORITE_BOOK,        // Thêm sách vào yêu thích
            Permission.DELETE_FAVORITE_BOOK,     // Xóa sách khỏi yêu thích

            // Genre management
            Permission.ADD_GENRE,             // Thêm thể loại
            Permission.EDIT_GENRE,            // Chỉnh sửa thể loại
            Permission.DELETE_GENRE           // Xóa thể loại
    };

    public static final Permission[] CUSTOMER_PERMISSIONS = new Permission[]{
            // Favorite book management
            Permission.VIEW_OWN_FAVORITE_BOOKS,  // Xem danh sách yêu thích của chính mình
            Permission.ADD_FAVORITE_BOOK,        // Thêm sách vào yêu thích
            Permission.DELETE_FAVORITE_BOOK,     // Xóa sách khỏi yêu thích

            // Book viewing
            Permission.VIEW_ALL_BOOK,            // Xem tất cả sách

            // Order management
            Permission.VIEW_OWN_ORDERS,          // Xem đơn hàng của mình
            Permission.VIEW_OWN_ORDER_DETAILS,   // Xem chi tiết đơn hàng của mình
            Permission.CREATE_ORDER,             // Tạo đơn hàng
            Permission.SEARCH_OWN_ORDERS         // Tìm kiếm đơn hàng của mình
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
