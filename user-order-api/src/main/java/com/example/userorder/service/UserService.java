package com.example.userorder.service;

import com.example.userorder.dto.LoginRequestDto;
import com.example.userorder.dto.LoginResponseDto;
import com.example.userorder.dto.UserRequestDto;
import com.example.userorder.dto.UserResponseDto;
import com.example.userorder.entity.User;
import com.example.userorder.exception.DuplicateLoginIdException;
import com.example.userorder.exception.InvalidLoginException;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.jwt.JwtProvider;
import com.example.userorder.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto request){
        if(userRepository.findByLoginId(request.getLoginId()).isPresent()){
            throw new DuplicateLoginIdException();
        }

        User user = new User(
                request.getName(),
                request.getAge(),
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword())
        );

        return new UserResponseDto(userRepository.save(user));
    }

    public LoginResponseDto login(LoginRequestDto request){
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(InvalidLoginException::new);

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidLoginException();
        }

        return new LoginResponseDto(jwtProvider.createToken(user.getLoginId()));
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setName(request.getName());
        user.setAge(request.getAge());

        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}