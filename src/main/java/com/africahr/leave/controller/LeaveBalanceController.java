package com.africahr.leave.controller;

import com.africahr.leave.dto.LeaveBalanceAdjustmentDto;
import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.service.LeaveAccrualService;
import com.africahr.leave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {
    private final LeaveAccrualService leaveAccrualService;
    private final UserService userService;

    @PostMapping("/adjust")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<LeaveBalance> adjustLeaveBalance(
            @Valid @RequestBody LeaveBalanceAdjustmentDto adjustment) {
        return ResponseEntity.ok(leaveAccrualService.adjustLeaveBalance(adjustment));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or #userId == authentication.principal.id")
    public ResponseEntity<List<LeaveBalance>> getUserLeaveBalances(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveAccrualService.getLeaveBalances(userService.getUserById(userId)));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<LeaveBalance>> getDepartmentLeaveBalances(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(leaveAccrualService.getLeaveBalancesByDepartment(departmentId));
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<LeaveBalance>> getLeaveBalancesByYear(@PathVariable int year) {
        return ResponseEntity.ok(leaveAccrualService.getLeaveBalancesByYear(year));
    }

    @GetMapping("/{userId}/{leaveTypeId}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or #userId == authentication.principal.id")
    public ResponseEntity<LeaveBalance> getLeaveBalance(
            @PathVariable Long userId,
            @PathVariable Long leaveTypeId,
            @PathVariable int year) {
        return ResponseEntity.ok(leaveAccrualService.getLeaveBalance(userId, leaveTypeId, year));
    }
} 