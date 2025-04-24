package com.africahr.leave.service;

import com.africahr.leave.dto.LeaveBalanceAdjustmentDto;
import com.africahr.leave.model.LeaveAccrualRule;
import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;

import java.time.LocalDate;
import java.util.List;

public interface LeaveAccrualService {
    void calculateAccruals(LocalDate calculationDate);
    void calculateAccrualsForUser(User user, LocalDate calculationDate);
    void applyCarryForward(User user, LeaveType leaveType, int year);
    List<LeaveBalance> getLeaveBalances(User user);
    LeaveAccrualRule createAccrualRule(LeaveAccrualRule rule);
    LeaveAccrualRule updateAccrualRule(Long id, LeaveAccrualRule rule);
    void deactivateAccrualRule(Long id);
    
    // New methods for manual adjustments
    LeaveBalance adjustLeaveBalance(LeaveBalanceAdjustmentDto adjustment);
    List<LeaveBalance> getLeaveBalancesByDepartment(Long departmentId);
    List<LeaveBalance> getLeaveBalancesByYear(int year);
    LeaveBalance getLeaveBalance(Long userId, Long leaveTypeId, int year);
} 