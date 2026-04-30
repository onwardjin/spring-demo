package com.example.userorder.controller;

import com.example.userorder.dto.*;
import com.example.userorder.entity.User;
import com.example.userorder.exception.InvalidLoginException;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.service.UserService;
import com.example.userorder.support.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                new UserCreateRequestDto("testLoginId", "testPassword", "testUserName", 100);
        // User user = User.createGeneralUser("testLoginId", "encodedPassword", "testName", 100);
        UserResponseDto response = new UserResponseDto("testLoginId", "testUserName", 100);

        when(userService.createUser(any(UserCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value(request.loginId()))
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.age").value(request.age()));
    }

    @Test
    @WithMockUser
    void createUser_withDuplicateLoginId_returnsBadRequest() throws Exception {
        UserCreateRequestDto request =
                new UserCreateRequestDto("testLoginId", "testPassword", "testUserName", 100);

        when(userService.createUser(any(UserCreateRequestDto.class)))
                .thenThrow(new InvalidLoginException());

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_success() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testLoginId", "testPassword");
        LoginResponseDto response = new LoginResponseDto("createdToken");

        when(userService.login(any(LoginRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("createdToken"));
    }


    @Test
    @WithMockUser
    void login_withInvalidCredentials_returnsBadRequest() throws Exception {
        LoginRequestDto request = new LoginRequestDto("testWrongLoginId", "testWrongPassword");

        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new InvalidLoginException());

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser
    void getUserInfo_withValidRequest() throws Exception {
        when(userService.getUserInfo(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    return new UserResponseDto(user.getLoginId(), user.getName(), user.getAge());
                });

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value("testLoginId"))
                .andExpect(jsonPath("$.name").value("testUserName"))
                .andExpect(jsonPath("$.age").value(100));
    }

    @Test
    @WithMockCustomUser
    void getUserInfo_whenUserNotFound_returnsNotFound() throws Exception {
        when(userService.getUserInfo(any(User.class)))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    void updateUser_success() throws Exception {
        UserUpdateRequestDto request =
                new UserUpdateRequestDto("updatedUserName", 101);
        UserResponseDto response =
                new UserResponseDto("testLoginId", "updatedUserName", 101);

        when(userService.updateUser(eq(1L), any(UserUpdateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value("testLoginId"))
                .andExpect(jsonPath("$.name").value("updatedUserName"))
                .andExpect(jsonPath("$.age").value(101));
    }

    @Test
    @WithMockCustomUser
    void updateUser_whenUserNotFound_returnsNotFound() throws Exception {
        UserUpdateRequestDto request =
                new UserUpdateRequestDto("updatedUserName", 101);

        when(userService.updateUser(eq(1L), any(UserUpdateRequestDto.class)))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/users/me")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockCustomUser
    void deleteUser_whenUserNotFound_returnsNotFound() throws Exception {
        doThrow(new UserNotFoundException())
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/me")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}