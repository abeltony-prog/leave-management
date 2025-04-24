package com.africahr.leave.service.impl;

import com.africahr.leave.dto.TeamCalendarDto;
import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.repository.LeaveRequestRepository;
import com.africahr.leave.service.TeamCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamCalendarServiceImpl implements TeamCalendarService {
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public List<TeamCalendarDto> getTeamLeaveCalendar(LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByStartDateBetweenAndStatus(
                startDate, endDate, LeaveStatus.APPROVED);
        
        return mapToTeamCalendarDto(leaveRequests);
    }

    @Override
    public List<TeamCalendarDto> getDepartmentLeaveCalendar(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.APPROVED);
        
        return mapToTeamCalendarDto(leaveRequests);
    }

    @Override
    public List<TeamCalendarDto> getTeamLeaveCalendarByUser(Long userId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserIdAndStartDateBetweenAndStatus(
                userId, startDate, endDate, LeaveStatus.APPROVED);
        
        return mapToTeamCalendarDto(leaveRequests);
    }

    private List<TeamCalendarDto> mapToTeamCalendarDto(List<LeaveRequest> leaveRequests) {
        return leaveRequests.stream()
                .map(request -> TeamCalendarDto.builder()
                        .leaveRequestId(request.getId())
                        .userId(request.getUser().getId())
                        .userFullName(request.getUser().getFirstName() + " " + request.getUser().getLastName())
                        .department(request.getUser().getDepartment().getName())
                        .leaveType(request.getLeaveType())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .halfDay(request.isHalfDay())
                        .status(request.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }
} 