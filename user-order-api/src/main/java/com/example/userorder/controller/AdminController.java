package com.example.userorder.controller;

import com.example.userorder.dto.RoleUpdateRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController{
    private final UserService userService;

    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public String adminOnly(){
        return "Admin only";
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAll(){
        return userService.getAllUsers();
    }

    @PatchMapping("/users/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequestDto request){
        return userService.updateRole(id, request);
    }
}
