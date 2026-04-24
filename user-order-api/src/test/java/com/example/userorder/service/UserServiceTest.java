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
                new UserCreateRequestDto("testId", "testPassword", "testName", 100);
        User user = User.createGeneralUser("testId", "encodedPassword", "testName", 100);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDto result = userService.createUser(request);

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(captor.capture());
        assertEquals("testId", captor.getValue().getLoginId());
        assertEquals("testName", captor.getValue().getName());
        assertEquals(100, captor.getValue().getAge());
    }

    @Test
    void createUser_withDuplicateLoginId_throwsDuplicateLoginException() {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testId", "testPassword", "testName", 100);
        User user = User.createGeneralUser("testId", "encodedPassword", "testName", 100);

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.of(user));

        assertThrows(DuplicateLoginIdException.class,
                () -> userService.createUser(request));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_withValidRequest_returnsLoginResponse() {
        LoginRequestDto request =
                new LoginRequestDto("testId", "testPassword");
        User user =
                User.createGeneralUser("testId", "encodedPassword", "testName", 100);


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
    void login_withNonExistingLoginId_throwsInvalidLoginException() {
        LoginRequestDto request =
                new LoginRequestDto("testId", "testPassword");

        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class,
                () -> userService.login(request));

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtProvider, never()).createToken(anyString());
    }

    @Test
    void login_withWrongPassword_throwsInvalidLoginException() {
        LoginRequestDto request =
                new LoginRequestDto("testId", "testWrongPassword");
        User user =
                User.createGeneralUser("testId", "encodedPassword", "testName", 100);


        when(userRepository.findByLoginId(request.loginId()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword()))
                .thenReturn(false);

        assertThrows(InvalidLoginException.class,
                () -> userService.login(request));

        verify(userRepository).findByLoginId(request.loginId());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(jwtProvider, never()).createToken(anyString());
    }

    @Test
    void updateUser_withValidRequest_returnsUserResponse() {
        Long principalUserId = 1L;
        UserUpdateRequestDto request =
                new UserUpdateRequestDto("testUpdateName", 10);
        User user = User.createGeneralUser("testId", "encodedPassword", "testName", 100);

        when(userRepository.findById(principalUserId))
                .thenReturn(Optional.of(user));

        UserResponseDto result = userService.updateUser(principalUserId, request);

        verify(userRepository).findById(principalUserId);
        verify(userRepository, never()).save(any(User.class));
        assertEquals(request.name(), result.name());
        assertEquals(request.age(), result.age());
    }

    @Test
    void updateUser_withNonExistingUser_throwsUserNotFoundException() {
        Long principalUserId = 1L;
        UserUpdateRequestDto request =
                new UserUpdateRequestDto("testUpdateName", 10);

        when(userRepository.findById(principalUserId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(principalUserId, request));
        verify(userRepository).findById(principalUserId);
    }

    @Test
    void deleteUser_withValidRequest_success() {
        Long principalUserId = 1L;
        User user =
                User.createGeneralUser("testId", "encodedPassword", "testName", 100);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findById(principalUserId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(principalUserId);

        verify(userRepository).findById(principalUserId);
        verify(userRepository).delete(captor.capture());
        assertEquals(user, captor.getValue());
    }

    @Test
    void deleteUser_withNonExistingUser_throwsUserNotFoundException() {
        Long principalUserId = 1L;

        when(userRepository.findById(principalUserId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(principalUserId));

        verify(userRepository).findById(principalUserId);
        verify(userRepository, never()).delete(any(User.class));
    }
}