package com.africahr.leave.service;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    byte[] generateLeaveBalanceReport(Long departmentId, int year);
    byte[] generateLeaveUsageReport(Long departmentId, LocalDate startDate, LocalDate endDate);
    byte[] generateLeaveAccrualReport(Long departmentId, int year);
    byte[] generateLeaveCarryForwardReport(int year);
    byte[] generateLeaveTrendReport(Long departmentId, int startYear, int endYear);
    Map<LeaveType, Long> getLeaveTypeDistribution(Long departmentId, LocalDate startDate, LocalDate endDate);
    Map<User, Long> getTopLeaveUsers(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> getPendingApprovalsReport(Long departmentId);
    List<LeaveRequest> getRejectedLeavesReport(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> getCancelledLeavesReport(Long departmentId, LocalDate startDate, LocalDate endDate);
} 