package com.africahr.leave.controller;

import com.africahr.leave.dto.UserDto;
import com.africahr.leave.model.Department;
import com.africahr.leave.model.User;
import com.africahr.leave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        User user = mapDtoToUser(userDto);
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        User user = mapDtoToUser(userDto);
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(userService.getUsersByDepartment(departmentId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<User>> getUsersByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(userService.getUsersByManager(managerId));
    }

    private User mapDtoToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());

        if (userDto.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(userDto.getDepartmentId());
            user.setDepartment(department);
        }
        if (userDto.getManagerId() != null) {
            User manager = new User();
            manager.setId(userDto.getManagerId());
            user.setManager(manager);
        }

        return user;
    }
} 