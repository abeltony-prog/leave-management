package com.africahr.leave.service;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.User;
import com.africahr.leave.model.LeaveCalendar;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TeamCalendarService {
    List<LeaveRequest> getTeamCalendar(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> getDepartmentCalendar(Long departmentId, LocalDate startDate, LocalDate endDate);
    Map<User, List<LeaveRequest>> getTeamLeaveSchedule(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> getTeamMembersOnLeave(Long departmentId, LocalDate date);
    List<LeaveRequest> getTeamMembersOnLeaveNextWeek(Long departmentId);
    List<LeaveRequest> getTeamMembersOnLeaveNextMonth(Long departmentId);
    List<LeaveCalendar> getTeamLeaveCalendar(LocalDate startDate, LocalDate endDate);
    List<LeaveCalendar> getDepartmentLeaveCalendar(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<LeaveCalendar> getTeamLeaveCalendarByUser(Long userId, LocalDate startDate, LocalDate endDate);
} 