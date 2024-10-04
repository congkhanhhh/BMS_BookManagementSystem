package com.bookstore.project.enumerated;

public enum Permission {
    //ADMIN Permissions
    VIEW_ALL_BOOK,        // Xem tất cả sách
    ADD_BOOK,             // Thêm sách mới
    EDIT_BOOK,            // Chỉnh sửa sách
    DELETE_BOOK,          // Xóa sách
    UPLOAD_IMAGE_BOOK,    // Tải ảnh lên cho sách
    VIEW_ALL_BOOK_BY_GENRE,  // Xem sách theo thể loại

    ADD_GENRE,            // Thêm thể loại
    EDIT_GENRE,           // Chỉnh sửa thể loại
    DELETE_GENRE,         // Xóa thể loại

    VIEW_USER_BOOK,       // Xem sách của người dùng

    VIEW_ORDER,           // Xem tất cả các đơn hàng
    VIEW_ORDER_DETAILS,        // Xem chi tiết đơn hàng
    CREATE_ORDER,         // Tạo đơn hàng
    EDIT_ORDER,           // Chỉnh sửa đơn hàng
    DELETE_ORDER,         // Xóa đơn hàng
    SEARCH_ORDER,         // Tìm kiếm đơn hàng

    //CUSTOMER Permissions
    VIEW_OWN_FAVORITE_BOOKS,
    VIEW_BOOK_BY_ID,
    ADD_FAVORITE_BOOK,    // Thêm sách vào yêu thích
    DELETE_FAVORITE_BOOK, // Xóa sách khỏi yêu thích
    VIEW_USER_FAVORITE_BOOK, // Xem danh sách yêu thích của người dùng


    // Customer Order Permissions
    VIEW_OWN_ORDERS,           // Xem danh sách tất cả các đơn hàng của chính mình
    VIEW_OWN_ORDER_DETAILS,    // Xem chi tiết đơn hàng của chính mình
    SEARCH_OWN_ORDERS          // Tìm kiếm đơn hàng của chính mình
}



