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
import org.apache.juli.logging.Log;
import org.springframework.dao.DataIntegrityViolationException;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto){
        if(userRepository.findByLoginId(requestDto.getLoginId()).isPresent()){
            throw new DuplicateLoginIdException();
        }
        User user = new User(
                requestDto.getName(),
                requestDto.getAge(),
                requestDto.getLoginId(),
                passwordEncoder.encode(requestDto.getPassword())
        );
        try{
            return new UserResponseDto(userRepository.save(user));
        } catch(DataIntegrityViolationException e){
            throw new DuplicateLoginIdException();
        }
    }

    public LoginResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(InvalidLoginException::new);
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new InvalidLoginException();
        }

        String token = jwtProvider.createToken(user.getLoginId());
        return new LoginResponseDto(token);
    }

    public List<UserResponseDto> readAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    public UserResponseDto readUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setName(requestDto.getName());
        user.setAge(requestDto.getAge());

        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}