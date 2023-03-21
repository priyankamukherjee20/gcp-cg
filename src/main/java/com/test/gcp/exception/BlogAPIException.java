package com.test.gcp.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

    private static final long serialVersionUID = 4221297461041814952L;
    private HttpStatus status;
    private String message;

    public BlogAPIException(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
