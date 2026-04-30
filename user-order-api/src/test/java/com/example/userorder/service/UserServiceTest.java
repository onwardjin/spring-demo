package com.example.userorder.service;

import com.example.userorder.dto.*;
import com.example.userorder.entity.User;
import com.example.userorder.exception.DuplicateLoginIdException;
import com.example.userorder.exception.InvalidLoginException;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.repository.UserRepository;
import com.example.userorder.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    void createUser_withValidRequest_returnsUserResponse() {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testLoginId", "testPassword", "testUserName", 100);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDto result = userService.createUser(request);

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(request.loginId(), savedUser.getLoginId());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(request.name(), savedUser.getName());
        assertEquals(request.age(), savedUser.getAge());

        assertEquals(request.loginId(), result.loginId());
        assertEquals(request.name(), result.name());
        assertEquals(request.age(), result.age());
    }

    @Test
    void createUser_withDuplicateLoginId_throwsDuplicateLoginException() {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testLoginId", "testPassword", "testUserName", 100);
        User user = User.createGeneralUser("testLoginId", "encodedPassword", "testUserName", 100);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.of(user));

        assertThrows(DuplicateLoginIdException.class, () -> userService.createUser(request));

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_withValidRequest_returnsLoginResponse() {
        LoginRequestDto request = new LoginRequestDto("testLoginId", "testPassword");
        User user = User.createGeneralUser("testLoginId", "encodedPassword", "testUserName", 100);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword()))
                .thenReturn(true);
        when(jwtProvider.createToken(user.getLoginId()))
                .thenReturn("createdToken");

        LoginResponseDto result = userService.login(request);

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(jwtProvider).createToken(user.getLoginId());
        assertEquals("createdToken", result.token());
    }

    @Test
    void login_withUserNotFound_throwsInvalidLoginException() {
        LoginRequestDto request = new LoginRequestDto("testLoginId", "testPassword");

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class, () -> userService.login(request));

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtProvider, never()).createToken(any());
    }

    @Test
    void login_withWrongPassword_throwsInvalidLoginException() {
        LoginRequestDto request = new LoginRequestDto("testLoginId", "wrongPassword");
        User user = User.createGeneralUser("testLoginId", "encodedPassword", "testUserName", 100);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidLoginException.class, () -> userService.login(request));
        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(jwtProvider, never()).createToken(any());
    }

    @Test
    void updateUser_withValidRequest_returnsUserResponse() {
        Long userId = 1L;
        UserUpdateRequestDto request = new UserUpdateRequestDto("testUpdatedName", 20);
        User user = User.createGeneralUser("testLoginId", "testPassword", "testName", 100);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResponseDto response = userService.updateUser(userId, request);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());

        assertEquals("testUpdatedName", response.name());
        assertEquals(20, response.age());
        assertEquals("testUpdatedName", user.getName());
        assertEquals(20, user.getAge());
    }

    @Test
    void updateUser_withUserNotFound_throwsUserNotFoundException() {
        Long userId = 1L;
        UserUpdateRequestDto request = new UserUpdateRequestDto("testUpdatedName", 20);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, request));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_withValidRequest_deleteUser() {
        Long userId = 1L;
        User user = User.createGeneralUser("testLoginId", "testPassword", "testName", 100);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_withUserNotFound_throwsUserNotFoundException() {
        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}