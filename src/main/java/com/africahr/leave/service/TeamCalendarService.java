package com.africahr.leave.service;

import com.africahr.leave.dto.TeamCalendarDto;

import java.time.LocalDate;
import java.util.List;

public interface TeamCalendarService {
    List<TeamCalendarDto> getTeamLeaveCalendar(LocalDate startDate, LocalDate endDate);
    List<TeamCalendarDto> getDepartmentLeaveCalendar(Long departmentId, LocalDate startDate, LocalDate endDate);
    List<TeamCalendarDto> getTeamLeaveCalendarByUser(Long userId, LocalDate startDate, LocalDate endDate);
} 