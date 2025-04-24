package com.africahr.leave.repository;

import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByUser(User user);
    Optional<LeaveBalance> findByUserAndLeaveTypeAndYear(User user, LeaveType leaveType, int year);
    List<LeaveBalance> findByUserAndYear(User user, int year);
} 