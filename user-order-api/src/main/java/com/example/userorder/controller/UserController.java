package com.example.userorder.controller;

import com.example.userorder.dto.*;
import com.example.userorder.security.CustomUserPrincipal;
import com.example.userorder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public UserResponseDto getUserInfo(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return userService.getUserInfo(principal.getUser());
    }

    @PatchMapping("/me")
    public UserResponseDto updateUser(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UserUpdateRequestDto request
    ) {
        return userService.updateUser(principal.getId(), request);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
        userService.deleteUser(principal.getId());
        return ResponseEntity.noContent().build();
    }
}