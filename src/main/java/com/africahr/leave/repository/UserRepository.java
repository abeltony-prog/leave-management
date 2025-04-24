package com.africahr.leave.repository;

import com.africahr.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByManager(User manager);
    List<User> findByDepartmentId(Long departmentId);
    boolean existsByEmail(String email);
    List<User> findByActiveTrue();
} 