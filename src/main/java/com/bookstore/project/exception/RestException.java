package com.bookstore.project.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class RestException extends RuntimeException {

    private static final long serialVersionUID = -7353359301457761850L;

    private HttpStatus status;

    private Map<String, Object> errors = new HashMap<>();

    public RestException() {
        super();
    }

    public RestException(HttpStatus status) {
        super(status.getReasonPhrase());
        this.status = status;
    }

    public RestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public RestException(String message) {
        super(message);
    }

    public RestException addError(String field, String message) {
        errors.put(field, message);
        return this;
    }

    public static RestException badRequest() {
        return new RestException(HttpStatus.BAD_REQUEST);
    }

    public static RestException badRequest(String message) {
        return new RestException(HttpStatus.BAD_REQUEST, message);
    }

    public static RestException unauthorized() {
        return new RestException(HttpStatus.UNAUTHORIZED);
    }

    public static RestException unauthorized(String message) {
        return new RestException(HttpStatus.UNAUTHORIZED, message);
    }

    public static RestException forbidden() {
        return new RestException(HttpStatus.FORBIDDEN);
    }

    public static RestException forbidden(String message) {
        return new RestException(HttpStatus.FORBIDDEN, message);
    }

    public static RestException notFound() {
        return new RestException(HttpStatus.NOT_FOUND);
    }

    public static RestException notFound(String message) {
        return new RestException(HttpStatus.NOT_FOUND, message);
    }

    public static RestException internalServerError() {
        return new RestException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static RestException internalServerError(String message) {
        return new RestException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
