package com.example.desafiosenai.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record Issue(
        String message,
        int statusCode,
        String statusName,
        LocalDateTime timestamp
) {
    public Issue(String message, HttpStatus status) {
        this(message, status.value(), status.getReasonPhrase(), LocalDateTime.now());
    }
}
