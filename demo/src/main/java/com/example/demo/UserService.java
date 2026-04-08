package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // CREATE
    public UserResponseDto createUser(UserRequestDto request){
        User user = new User(request.getName(), request.getAge());
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getName(), savedUser.getAge());
    }

    // READ ALL
    public List<UserResponseDto> readAllUser(){
        return userRepository.findAll().stream()
                .map(u->new UserResponseDto(u.getName(), u.getAge()))
                .toList();

    }
    // READ ONE
    public UserResponseDto readUser(long id){
        return userRepository.findById(id)
                .map(u->new UserResponseDto(u.getName(), u.getAge()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // UPDATE
    public UserResponseDto updateUser(long id, UserRequestDto request){
        User u = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
        u.setName(request.getName());
        u.setAge(request.getAge());
        User updatedUser = userRepository.save(u);

        return new UserResponseDto(updatedUser.getName(), updatedUser.getAge());
    }

    // DELETE
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}