package com.africahr.leave.controller;

import com.africahr.leave.dto.LeaveBalanceDto;
import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import com.africahr.leave.service.LeaveBalanceService;
import com.africahr.leave.service.LeaveTypeService;
import com.africahr.leave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveTypeService leaveTypeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<LeaveBalance> createLeaveBalance(@Valid @RequestBody LeaveBalanceDto leaveBalanceDto) {
        User user = userService.getUserById(leaveBalanceDto.getUserId());
        LeaveType leaveType = leaveTypeService.getLeaveTypeById(leaveBalanceDto.getLeaveTypeId());
        
        LeaveBalance leaveBalance = LeaveBalance.builder()
            .user(user)
            .leaveType(leaveType)
            .totalDays(leaveBalanceDto.getTotalDays())
            .usedDays(leaveBalanceDto.getUsedDays())
            .remainingDays(leaveBalanceDto.getRemainingDays())
            .carriedForwardDays(leaveBalanceDto.getCarriedForwardDays())
            .year(leaveBalanceDto.getYear())
            .lastUpdated(leaveBalanceDto.getLastUpdated())
            .build();
            
        return ResponseEntity.ok(leaveBalanceService.createLeaveBalance(leaveBalance));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveBalance> updateLeaveBalance(
            @PathVariable Long id,
            @Valid @RequestBody LeaveBalanceDto leaveBalanceDto) {
        User user = userService.getUserById(leaveBalanceDto.getUserId());
        LeaveType leaveType = leaveTypeService.getLeaveTypeById(leaveBalanceDto.getLeaveTypeId());
        
        LeaveBalance leaveBalance = LeaveBalance.builder()
            .id(id)
            .user(user)
            .leaveType(leaveType)
            .totalDays(leaveBalanceDto.getTotalDays())
            .usedDays(leaveBalanceDto.getUsedDays())
            .remainingDays(leaveBalanceDto.getRemainingDays())
            .carriedForwardDays(leaveBalanceDto.getCarriedForwardDays())
            .year(leaveBalanceDto.getYear())
            .lastUpdated(leaveBalanceDto.getLastUpdated())
            .build();
            
        return ResponseEntity.ok(leaveBalanceService.updateLeaveBalance(id, leaveBalance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveBalance(@PathVariable Long id) {
        leaveBalanceService.deleteLeaveBalance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveBalance> getLeaveBalanceById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalanceById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveBalance>> getLeaveBalancesByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalancesByUser(user));
    }

    @GetMapping("/user/{userId}/type/{leaveType}/year/{year}")
    public ResponseEntity<LeaveBalance> getLeaveBalanceByUserAndTypeAndYear(
            @PathVariable Long userId,
            @PathVariable String leaveType,
            @PathVariable int year) {
        User user = userService.getUserById(userId);
        LeaveType type = leaveTypeService.getLeaveTypeByName(leaveType);
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalanceByUserAndTypeAndYear(user, type, year));
    }

    @GetMapping("/user/{userId}/year/{year}")
    public ResponseEntity<List<LeaveBalance>> getLeaveBalancesByUserAndYear(
            @PathVariable Long userId,
            @PathVariable int year) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(leaveBalanceService.getLeaveBalancesByUserAndYear(user, year));
    }

    @PutMapping("/{id}/adjust")
    public ResponseEntity<LeaveBalance> adjustLeaveBalance(
            @PathVariable Long id,
            @RequestParam double days,
            @RequestParam String operation) {
        return ResponseEntity.ok(leaveBalanceService.adjustLeaveBalance(id, days));
    }
} 