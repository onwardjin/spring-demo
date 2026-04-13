package com.example.userorder.service;

import com.example.userorder.dto.UserRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.entity.User;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService{
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto request){
        User user = new User(
                request.getName(),
                request.getAge(),
                request.getLoginId(),
                request.getPassword()
        );
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id){
        return userRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto request){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setName(request.getName());
        user.setAge(request.getAge());

        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}