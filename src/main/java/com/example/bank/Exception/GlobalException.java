package com.example.bank.Exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
