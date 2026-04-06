package com.example.demo;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    List<User> users = new ArrayList<>(); // Fake DATABASE
    long nextId = 1;


    // CRUD 구현

    // CREATE: DB저장, 결과값반환
    public UserResponseDto createUser(UserRequestDto request){
        User user = new User(nextId++, request.getName(), request.getAge());
        users.add(user);
        return new UserResponseDto(user.getName(), user.getAge());
    }

    // READ01: 전체조회
    public List<UserResponseDto> readAllUsers(){
        List<UserResponseDto> result = new ArrayList<>();
        for(User u:users){
            result.add(new UserResponseDto(u.getName(), u.getAge()));
        }
        return result;
    }

    // READ02: DB조회, id기준 조회
    public UserResponseDto readUser(long id){
        for(User u:users){
            if(u.getId() == id) return new UserResponseDto(u.getName(), u.getAge());
        }
        return null;
    }

    // UPDATE: DB항목 수정, 결과값반환
    public UserResponseDto updateUser(long id, UserRequestDto request){
        for(User u:users){
            if(u.getId() == id){
                u.setName(request.getName());
                u.setAge(request.getAge());
                return new UserResponseDto(u.getName(), u.getAge());
            }
        }
        return null;
    }

    // DELETE: DB항목 삭제, 결과 반환없음
    public void deleteUser(long id){
        Iterator<User> it = users.iterator();

        while(it.hasNext()){
            User u = it.next();

            if(u.getId() == id){
                it.remove();
                return;
            }
        }
    }
}
