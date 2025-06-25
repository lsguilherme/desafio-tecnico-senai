package com.example.desafiosenai.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Issue> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String message = "Validation failed: " + errors.toString();
        Issue errorResponse = new Issue(message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Issue> handleResourceNotFoundException(ResourceNotFoundException e){
        Issue issue = new Issue(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(issue, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Issue> handleBadRequestException(BadRequestException e){
        Issue issue = new Issue(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(issue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Issue> handleConflictException(ConflictException e){
        Issue issue = new Issue(e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(issue, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<Issue> handleUnprocessableEntityException(UnprocessableEntityException e){
        Issue issue = new Issue(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(issue, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Issue> handleRuntimeException(RuntimeException e) {
        Issue issue = new Issue("Ocorreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(issue, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
