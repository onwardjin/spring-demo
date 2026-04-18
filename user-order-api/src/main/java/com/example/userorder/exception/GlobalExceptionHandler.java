package com.example.userorder.exception;

import com.example.userorder.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ErrorResponseDto> handleInvalidToken(InvalidTokenException e){
        ErrorResponseDto response = new ErrorResponseDto(401, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidLoginException.class)
    ResponseEntity<ErrorResponseDto> handleInvalidLogin(InvalidLoginException e){
        ErrorResponseDto response = new ErrorResponseDto(401, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicateLoginIdException.class)
    ResponseEntity<ErrorResponseDto> handleDuplicateLoginId(DuplicateLoginIdException e) {
        ErrorResponseDto response = new ErrorResponseDto(409, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e){
        ErrorResponseDto response = new ErrorResponseDto(404, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OrderNotFoundExeption.class)
    ResponseEntity<ErrorResponseDto> handleOrderNotFound(OrderNotFoundExeption e){
        ErrorResponseDto response = new ErrorResponseDto(404, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}