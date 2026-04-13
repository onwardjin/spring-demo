package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController{
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto request){
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponseDto> readAllUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto readUser(@PathVariable long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable long id,
                                      @Valid @RequestBody UserRequestDto request){
        return userService.updateUser(id, request);
    }

    @DeleteMapping
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }
}