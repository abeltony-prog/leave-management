package com.africahr.leave.repository;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUser(User user);
    List<LeaveRequest> findByUserAndStatus(User user, LeaveStatus status);
    List<LeaveRequest> findByApproverAndStatus(User approver, LeaveStatus status);
    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, LeaveStatus status);
    List<LeaveRequest> findByUserDepartmentIdAndStartDateBetweenAndStatus(Long departmentId, LocalDate startDate, LocalDate endDate, LeaveStatus status);
    List<LeaveRequest> findByUserIdAndStartDateBetweenAndStatus(Long userId, LocalDate startDate, LocalDate endDate, LeaveStatus status);
} 