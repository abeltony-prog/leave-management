package com.africahr.leave.controller;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import com.africahr.leave.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/leave-balance/{departmentId}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<byte[]> generateLeaveBalanceReport(
            @PathVariable Long departmentId,
            @PathVariable int year) {
        byte[] report = reportService.generateLeaveBalanceReport(departmentId, year);
        return createExcelResponse(report, "leave-balance-report.xlsx");
    }

    @GetMapping("/leave-usage/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<byte[]> generateLeaveUsageReport(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] report = reportService.generateLeaveUsageReport(departmentId, startDate, endDate);
        return createExcelResponse(report, "leave-usage-report.xlsx");
    }

    @GetMapping("/leave-accrual/{departmentId}/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<byte[]> generateLeaveAccrualReport(
            @PathVariable Long departmentId,
            @PathVariable int year) {
        byte[] report = reportService.generateLeaveAccrualReport(departmentId, year);
        return createExcelResponse(report, "leave-accrual-report.xlsx");
    }

    @GetMapping("/leave-carry-forward/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<byte[]> generateLeaveCarryForwardReport(@PathVariable int year) {
        byte[] report = reportService.generateLeaveCarryForwardReport(year);
        return createExcelResponse(report, "leave-carry-forward-report.xlsx");
    }

    @GetMapping("/leave-trend/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<byte[]> generateLeaveTrendReport(
            @PathVariable Long departmentId,
            @RequestParam int startYear,
            @RequestParam int endYear) {
        byte[] report = reportService.generateLeaveTrendReport(departmentId, startYear, endYear);
        return createExcelResponse(report, "leave-trend-report.xlsx");
    }

    @GetMapping("/leave-type-distribution/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Map<LeaveType, Long>> getLeaveTypeDistribution(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getLeaveTypeDistribution(departmentId, startDate, endDate));
    }

    @GetMapping("/top-leave-users/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Map<User, Long>> getTopLeaveUsers(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getTopLeaveUsers(departmentId, startDate, endDate));
    }

    @GetMapping("/pending-approvals/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public ResponseEntity<List<LeaveRequest>> getPendingApprovalsReport(@PathVariable Long departmentId) {
        return ResponseEntity.ok(reportService.getPendingApprovalsReport(departmentId));
    }

    @GetMapping("/rejected-leaves/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<LeaveRequest>> getRejectedLeavesReport(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getRejectedLeavesReport(departmentId, startDate, endDate));
    }

    @GetMapping("/cancelled-leaves/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<LeaveRequest>> getCancelledLeavesReport(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getCancelledLeavesReport(departmentId, startDate, endDate));
    }

    private ResponseEntity<byte[]> createExcelResponse(byte[] report, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok()
                .headers(headers)
                .body(report);
    }
} 