package com.example.userorder.service;

import com.example.userorder.dto.*;
import com.example.userorder.entity.User;
import com.example.userorder.exception.DuplicateLoginIdException;
import com.example.userorder.exception.InvalidLoginException;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.repository.UserRepository;
import com.example.userorder.security.JwtProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto request) {
        if (userRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new DuplicateLoginIdException();
        }

        User user = User.createGeneralUser(
                request.loginId(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.age()
        );

        try {
            User savedUser = userRepository.save(user);
            return new UserResponseDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLoginIdException();
        }
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(InvalidLoginException::new);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        String token = jwtProvider.createToken(user.getLoginId());
        return new LoginResponseDto(token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public UserResponseDto getUserInfo(User user) {
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.updateInfo(request.name(), request.age());
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}