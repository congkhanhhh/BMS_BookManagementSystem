package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteBookRequest {
    private Long userId;
    private Long bookId;
}
