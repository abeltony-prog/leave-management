package com.africahr.leave.controller;

import com.africahr.leave.dto.TeamCalendarDto;
import com.africahr.leave.service.TeamCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/team-calendar")
@RequiredArgsConstructor
public class TeamCalendarController {
    private final TeamCalendarService teamCalendarService;

    @GetMapping
    public ResponseEntity<List<TeamCalendarDto>> getTeamCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getTeamLeaveCalendar(startDate, endDate));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<TeamCalendarDto>> getDepartmentCalendar(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getDepartmentLeaveCalendar(departmentId, startDate, endDate));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamCalendarDto>> getUserCalendar(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(teamCalendarService.getTeamLeaveCalendarByUser(userId, startDate, endDate));
    }
} 