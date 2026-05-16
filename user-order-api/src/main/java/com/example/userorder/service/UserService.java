package com.example.userorder.service;

import com.example.userorder.common.exception.DuplicateLoginIdException;
import com.example.userorder.common.exception.InvalidLoginException;
import com.example.userorder.common.exception.UserNotFoundException;
import com.example.userorder.domain.user.User;
import com.example.userorder.domain.user.UserProfile;
import com.example.userorder.domain.user.vo.*;
import com.example.userorder.dto.auth.LoginRequest;
import com.example.userorder.dto.auth.LoginResponse;
import com.example.userorder.dto.auth.PasswordUpdateRequest;
import com.example.userorder.dto.user.*;
import com.example.userorder.mapper.UserMapper;
import com.example.userorder.repository.UserRepository;
import com.example.userorder.security.JwtProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long createUser(UserCreateRequest request) {
        LoginId loginId = LoginId.of(request.loginId());

        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Password password = Password.of(encodedPassword);

        User user = User.createUserWithProfile(
                loginId,
                password,
                request.name() != null ? UserName.of(request.name()) : null,
                request.birthDate() != null ? BirthDate.of(request.birthDate()) : null,
                request.email() != null ? Email.of(request.email()) : null
        );

        try {
            userRepository.save(user);
            return user.getId();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLoginIdException();
        }
    }

    public LoginResponse login(LoginRequest request) {
        LoginId loginId = LoginId.of(request.loginId());

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(InvalidLoginException::new);

        if (!user.isPasswordMatcher(passwordEncoder, request.password())) {
            throw new InvalidLoginException();
        }

        String token = jwtProvider.createToken(user.getId(), user.getLoginId(), user.getRole());
        return new LoginResponse(token);
    }

    public UserResponse getUser(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::from)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword().value())) {
            throw new IllegalArgumentException();
        }

        String password = passwordEncoder.encode(request.newPassword());
        user.updatePassword(Password.of(password));
    }

    @Transactional
    public void updateProfile(Long userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.updateProfile(
                request.name() != null ? UserName.of(request.name()) : null,
                request.birthDate() != null ? BirthDate.of(request.birthDate()) : null,
                request.email() != null ? Email.of(request.email()) : null
        );
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}