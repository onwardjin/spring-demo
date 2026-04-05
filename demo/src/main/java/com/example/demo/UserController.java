package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto request){
        return userService.createUser(request);
    }
}

/*
- URL 요청 받기
- 요청 데이터를 DTO로 받기
- Service 호출
@RestController: 매서드의 반환값을 응답 본문으로 바로 보냄(문자열->문자열 / 객체->JSON)
@RequestMapping("/user"): 컨트롤러의 전체 기본 주소
@PostMapping: /users 요청 시 해당 메서드 실행
@RequestBody: 요청 본문(JSON)을 UserRequestDto 객체로 변환
 */