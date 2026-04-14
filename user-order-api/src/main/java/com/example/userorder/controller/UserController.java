package com.example.userorder.controller;

import com.example.userorder.dto.LoginRequestDto;
import com.example.userorder.dto.LoginResponseDto;
import com.example.userorder.dto.UserRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController{
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto){
        return userService.createUser(requestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto){
        return userService.login(requestDto);
    }

    @GetMapping
    public List<UserResponseDto> readAllUsers(){
        return userService.readAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto readUser(@PathVariable Long id){
        return userService.readUser(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto requestDto){
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}