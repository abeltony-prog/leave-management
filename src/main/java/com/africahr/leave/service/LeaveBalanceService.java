package com.africahr.leave.service;

import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;

import java.util.List;

public interface LeaveBalanceService {
    LeaveBalance createLeaveBalance(LeaveBalance leaveBalance);
    LeaveBalance updateLeaveBalance(Long id, LeaveBalance leaveBalance);
    void deleteLeaveBalance(Long id);
    LeaveBalance getLeaveBalanceById(Long id);
    List<LeaveBalance> getLeaveBalancesByUser(User user);
    LeaveBalance getLeaveBalanceByUserAndTypeAndYear(User user, LeaveType leaveType, int year);
    List<LeaveBalance> getLeaveBalancesByUserAndYear(User user, int year);
    LeaveBalance adjustLeaveBalance(Long id, double adjustment);
} 