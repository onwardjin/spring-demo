package com.example.demo.dto;

import java.util.List;

public class ErrorResponseDto {
    private int status;
    private List<String> errors;

    public ErrorResponseDto(int status, List<String> errors){
        this.status = status;
        this.errors = errors;
    }

    public List<String> getMessage(){
        return errors;
    }
}
