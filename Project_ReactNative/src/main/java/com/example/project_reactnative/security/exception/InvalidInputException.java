package com.example.project_reactnative.security.exception;

// Bạn có thể đặt tên là ValidationException, BadRequestException, v.v.
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}