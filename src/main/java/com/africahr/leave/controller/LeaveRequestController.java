package com.africahr.leave.controller;

import com.africahr.leave.dto.LeaveRequestDto;
import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import com.africahr.leave.service.LeaveRequestService;
import com.africahr.leave.service.LeaveTypeService;
import com.africahr.leave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserService userService;
    private final LeaveTypeService leaveTypeService;

    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@Valid @RequestBody LeaveRequestDto leaveRequestDto) {
        User requestingUser = userService.getUserById(leaveRequestDto.getUserId());
        LeaveRequest leaveRequest = mapDtoToLeaveRequest(leaveRequestDto, requestingUser);
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setCreatedAt(LocalDateTime.now());
        leaveRequest.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(leaveRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDto leaveRequestDto) {
        LeaveRequest existingRequest = leaveRequestService.getLeaveRequestById(id);
        User requestingUser = userService.getUserById(leaveRequestDto.getUserId());
        LeaveRequest updatedRequest = mapDtoToLeaveRequest(leaveRequestDto, requestingUser);

        updatedRequest.setId(id);
        updatedRequest.setCreatedAt(existingRequest.getCreatedAt());
        updatedRequest.setStatus(existingRequest.getStatus());
        updatedRequest.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(leaveRequestService.updateLeaveRequest(id, updatedRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByUser(user));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeaveRequest(
            @PathVariable Long id,
            @RequestParam String comment) {
        User approver = getCurrentUser();
        return ResponseEntity.ok(leaveRequestService.approveLeaveRequest(id, approver, comment));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> rejectLeaveRequest(
            @PathVariable Long id,
            @RequestParam String comment) {
        User rejector = getCurrentUser();
        return ResponseEntity.ok(leaveRequestService.rejectLeaveRequest(id, rejector, comment));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequest> cancelLeaveRequest(@PathVariable Long id) {
        User canceller = getCurrentUser();
        return ResponseEntity.ok(leaveRequestService.cancelLeaveRequest(id, canceller));
    }

    private LeaveRequest mapDtoToLeaveRequest(LeaveRequestDto dto, User user) {
        LeaveRequest request = new LeaveRequest();
        request.setUser(user);
        request.setLeaveType(leaveTypeService.getLeaveTypeByName(dto.getLeaveType()));
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setHalfDay(dto.isHalfDay());
        request.setReason(dto.getReason());
        return request;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }
        String userEmail = (String) authentication.getPrincipal();
        return userService.getUserByEmail(userEmail);
    }
} 