package com.africahr.leave.controller;

import com.africahr.leave.dto.LoginRequest;
import com.africahr.leave.dto.LoginResponse;
import com.africahr.leave.dto.UserDto;
import com.africahr.leave.model.Department;
import com.africahr.leave.model.User;
import com.africahr.leave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticateUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(user.getId(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody UserDto userDto) {
        if (userService.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = mapDtoToUser(userDto);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(new LoginResponse(createdUser.getId(), createdUser.getEmail(), createdUser.getRole()));
    }

    private User mapDtoToUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .department(Department.builder().id(userDto.getDepartmentId()).build())
                .manager(userDto.getManagerId() != null ? User.builder().id(userDto.getManagerId()).build() : null)
                .role(userDto.getRole())
                .build();
    }
} 