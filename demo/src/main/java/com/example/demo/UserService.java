package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserResponseDto createUser(UserRequestDto request){
        User user = new User(request.getName(), request.getAge());
        return new UserResponseDto(user.getName(), user.getAge());
    }
}

/*
- UserController가 받은 요청을 실제로 처리
- 내부 객체 생성
- 응답 객체로 변환
- 컨트롤러는 얇게 두고 서비스에 처리 로직을 넘김
-
 */