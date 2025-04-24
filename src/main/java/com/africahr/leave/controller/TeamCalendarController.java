package com.africahr.leave.controller;

import com.africahr.leave.dto.TeamCalendarDto;
import com.africahr.leave.service.TeamCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team-calendar")
@RequiredArgsConstructor
public class TeamCalendarController {

    private final TeamCalendarService teamCalendarService;

    @GetMapping
    public ResponseEntity<List<TeamCalendarDto>> getTeamLeaveCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getTeamLeaveCalendar(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<TeamCalendarDto>> getDepartmentLeaveCalendar(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getDepartmentLeaveCalendar(departmentId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamCalendarDto>> getTeamLeaveCalendarByUser(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getTeamLeaveCalendarByUser(userId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private TeamCalendarDto convertToDto(com.africahr.leave.model.LeaveCalendar calendar) {
        return TeamCalendarDto.builder()
                .id(calendar.getId())
                .userId(calendar.getUser().getId())
                .userName(calendar.getUser().getFullName())
                .leaveRequestId(calendar.getLeaveRequest().getId())
                .date(calendar.getDate())
                .isFullDay(calendar.isFullDay())
                .description(calendar.getDescription())
                .build();
    }
} 