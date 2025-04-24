package com.africahr.leave.service;

import com.africahr.leave.model.LeaveType;
import java.util.List;

public interface LeaveTypeService {
    LeaveType createLeaveType(LeaveType leaveType);
    LeaveType updateLeaveType(Long id, LeaveType leaveType);
    void deleteLeaveType(Long id);
    LeaveType getLeaveTypeById(Long id);
    LeaveType getLeaveTypeByName(String name);
    List<LeaveType> getAllLeaveTypes();
} 