package com.example.userorder.controller;

import com.example.userorder.dto.LoginRequestDto;
import com.example.userorder.dto.LoginResponseDto;
import com.example.userorder.dto.UserCreateRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.exception.DuplicateLoginIdException;
import com.example.userorder.exception.InvalidLoginException;
import com.example.userorder.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void createUser_success() throws Exception {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testId", "testPassword", "testName", 100);
        UserResponseDto result =
                new UserResponseDto("testId", "testName", 100);

        when(userService.createUser(request))
                .thenReturn(result);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value(request.loginId()))
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.age").value(request.age()));
        verify(userService).createUser(request);
    }

    @Test
    @WithMockUser
    void createUser_withDuplicateLoginId_returnsConflict() throws Exception {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testId", "testPassword", "testName", 100);

        when(userService.createUser(request))
                .thenThrow(new DuplicateLoginIdException());

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
        verify(userService).createUser(request);
    }

    @Test
    @WithMockUser
    void createUser_withInvalidRequest_returnsBadRequest() throws Exception {
        UserCreateRequestDto requestDto =
                new UserCreateRequestDto("testId", "testPassword", "testName", -100);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).createUser(any(UserCreateRequestDto.class));
    }

    @Test
    @WithMockUser
    void login_withValidRequest_returnsToken() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testId", "testPassword");
        LoginResponseDto response = new LoginResponseDto("createdToken");

        when(userService.login(request))
                .thenReturn(response);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.token()));
        verify(userService).login(request);
    }

    @Test
    @WithMockUser
    void login_withInvalidCredentials_returnsBadRequest() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testId", "testPassword");

        when(userService.login(request))
                .thenThrow(new InvalidLoginException());

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()
                );

        verify(userService).login(request);
    }

    @Test
    @WithMockUser
    void login_withInvalidRequest_returnsBadRequest() throws Exception {
        LoginRequestDto request = new LoginRequestDto("", "testPassword");

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).login(any());
    }
}
