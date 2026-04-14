package com.example.userorder.exception;

public class DuplicateLoginIdException extends RuntimeException {
    public DuplicateLoginIdException() {
        super("Login ID is duplicated");
    }
}
