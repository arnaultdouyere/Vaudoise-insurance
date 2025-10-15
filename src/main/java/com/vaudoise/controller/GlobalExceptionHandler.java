package com.vaudoise.controller;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<?> badReq(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
