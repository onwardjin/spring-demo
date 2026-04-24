package com.example.userorder.exception;

public class DuplicateLoginIdException extends RuntimeException {
    public DuplicateLoginIdException() {
        super("Login ID already exists");
    }

    public DuplicateLoginIdException(String message) {
        super(message);
    }
}
