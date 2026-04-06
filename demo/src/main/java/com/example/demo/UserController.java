package com.example.demo;

import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController{
    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    // Service로 넘기는 메서드들

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto request){
        return userService.createUser(request);
    }

    // 전체 조회
    @GetMapping
    public List<UserResponseDto> readAllUsers(){
        return userService.readAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto readUser(@PathVariable long id){ // 뭔지 모르겠음...
        return userService.readUser(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable long id, @RequestBody UserRequestDto request){
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }
}