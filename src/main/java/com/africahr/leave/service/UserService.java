package com.africahr.leave.service;

import com.africahr.leave.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByDepartment(Long departmentId);
    List<User> getUsersByManager(Long managerId);
    boolean existsByEmail(String email);
    User authenticateUser(String email, String password);
} 