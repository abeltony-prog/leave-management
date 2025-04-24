package com.africahr.leave.service.impl;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.User;
import com.africahr.leave.repository.LeaveRequestRepository;
import com.africahr.leave.repository.UserRepository;
import com.africahr.leave.service.TeamCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamCalendarServiceImpl implements TeamCalendarService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<LeaveRequest> getTeamCalendar(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.APPROVED);
    }

    @Override
    public List<LeaveRequest> getDepartmentCalendar(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.APPROVED);
    }

    @Override
    public Map<User, List<LeaveRequest>> getTeamLeaveSchedule(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<User> teamMembers = userRepository.findByDepartmentId(departmentId);
        return teamMembers.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> leaveRequestRepository.findByUserAndStartDateBetweenAndStatus(
                                user, startDate, endDate, LeaveStatus.APPROVED),
                        (existing, replacement) -> existing,
                        () -> Map.of()
                ));
    }

    @Override
    public List<LeaveRequest> getTeamMembersOnLeave(Long departmentId, LocalDate date) {
        return leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, date, date, LeaveStatus.APPROVED);
    }

    @Override
    public List<LeaveRequest> getTeamMembersOnLeaveNextWeek(Long departmentId) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(7);
        return getTeamCalendar(departmentId, startDate, endDate);
    }

    @Override
    public List<LeaveRequest> getTeamMembersOnLeaveNextMonth(Long departmentId) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusMonths(1);
        return getTeamCalendar(departmentId, startDate, endDate);
    }
} 