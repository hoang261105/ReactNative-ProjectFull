package com.example.project_reactnative.advice_controller;

import com.example.project_reactnative.model.dto.response.APIResponse;
import com.example.project_reactnative.security.exception.CustomValidationException;
import com.example.project_reactnative.security.exception.NoDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, CustomValidationException.class})
    public ResponseEntity<APIResponse<Object>> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException e) {
            e.getBindingResult().getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
        }

        if (ex instanceof CustomValidationException e) {
            errors.putAll(e.getErrors());
        }

        APIResponse<Object> response = new APIResponse<>(
                false,
                "Dữ liệu không hợp lệ",
                null,
                errors,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<APIResponse<Object>> handleNotFoundException(NoSuchElementException ex) {
        APIResponse<Object> response = new APIResponse<>(
                false,
                ex.getMessage(),
                null,
                null,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Object>> accessDeniedException(AccessDeniedException ex) {
        APIResponse<Object> response = new APIResponse<>(
                false,
                ex.getMessage(),
                null,
                null,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<APIResponse<Object>> noDataException(NoDataException ex) {
        APIResponse<Object> response = new APIResponse<>(
                false,
                ex.getMessage(),
                null,
                null,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}