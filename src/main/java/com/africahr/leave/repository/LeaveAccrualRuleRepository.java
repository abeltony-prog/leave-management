package com.africahr.leave.repository;

import com.africahr.leave.model.LeaveAccrualRule;
import com.africahr.leave.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveAccrualRuleRepository extends JpaRepository<LeaveAccrualRule, Long> {
    List<LeaveAccrualRule> findByActiveTrue();
    Optional<LeaveAccrualRule> findByLeaveTypeAndActiveTrue(LeaveType leaveType);
    List<LeaveAccrualRule> findByLeaveType(LeaveType leaveType);
} 