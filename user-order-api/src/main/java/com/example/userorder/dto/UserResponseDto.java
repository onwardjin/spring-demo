package com.example.userorder.dto;

import com.example.userorder.entity.User;

public record UserResponseDto(
        String loginId,
        String name,
        Integer age
) {
    public UserResponseDto(User user) {
        this(
                user.getLoginId(),
                user.getName(),
                user.getAge()
        );
    }
}