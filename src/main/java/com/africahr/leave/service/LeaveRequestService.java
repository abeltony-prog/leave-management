package com.africahr.leave.service;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.User;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {
    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
    LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest);
    void deleteLeaveRequest(Long id);
    LeaveRequest getLeaveRequestById(Long id);
    List<LeaveRequest> getLeaveRequestsByUser(User user);
    List<LeaveRequest> getLeaveRequestsByStatus(LeaveStatus status);
    List<LeaveRequest> getLeaveRequestsByApprover(User approver);
    List<LeaveRequest> getLeaveRequestsByDateRange(LocalDate startDate, LocalDate endDate);
    LeaveRequest approveLeaveRequest(Long id, User approver, String comment);
    LeaveRequest rejectLeaveRequest(Long id, User approver, String comment);
    LeaveRequest cancelLeaveRequest(Long id, User user);
} 