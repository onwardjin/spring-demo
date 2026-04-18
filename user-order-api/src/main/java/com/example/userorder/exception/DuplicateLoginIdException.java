package com.example.userorder.exception;

public class DuplicateLoginIdException extends RuntimeException {
    public DuplicateLoginIdException() {
        super("Duplicate ID");
    }
}
