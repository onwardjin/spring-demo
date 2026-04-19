package com.example.userorder.controller;

import com.example.userorder.dto.LoginRequestDto;
import com.example.userorder.dto.LoginResponseDto;
import com.example.userorder.dto.UserRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.security.CustomUserPrincipal;
import com.example.userorder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserRequestDto request){
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request){
        return userService.login(request);
    }

    @PutMapping("/me")
    public UserResponseDto update(@AuthenticationPrincipal CustomUserPrincipal user, @Valid @RequestBody UserRequestDto request){
        return userService.updateUser(user.getId(), request);
    }

    @DeleteMapping("/me")
    public void delete(@AuthenticationPrincipal CustomUserPrincipal user){
        userService.deleteUser(user.getId());
    }
}