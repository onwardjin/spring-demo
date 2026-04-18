package com.example.userorder.dto;

public class ErrorResponseDto{
    private Integer status;
    private String message;

    public ErrorResponseDto(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    public Integer getStatus(){ return status; }
    public String getMessage(){ return message; }
}
