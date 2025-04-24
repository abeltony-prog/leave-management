package com.africahr.leave.service.impl;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import com.africahr.leave.repository.LeaveRequestRepository;
import com.africahr.leave.repository.UserRepository;
import com.africahr.leave.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    @Override
    public byte[] generateLeaveBalanceReport(Long departmentId, int year) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leave Balance Report");
            createHeaderRow(sheet, "Employee", "Leave Type", "Total Days", "Used Days", "Remaining Days", "Carried Forward");

            List<User> users = userRepository.findByDepartmentId(departmentId);
            int rowNum = 1;

            for (User user : users) {
                List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserAndStartDateBetween(
                        user, LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));

                Map<LeaveType, Long> leaveTypeCounts = leaveRequests.stream()
                        .collect(Collectors.groupingBy(
                                LeaveRequest::getLeaveType,
                                Collectors.summingLong(req -> req.getEndDate().toEpochDay() - req.getStartDate().toEpochDay() + 1)
                        ));

                for (Map.Entry<LeaveType, Long> entry : leaveTypeCounts.entrySet()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getFullName());
                    row.createCell(1).setCellValue(entry.getKey().getName());
                    row.createCell(2).setCellValue(entry.getKey().getDefaultDays());
                    row.createCell(3).setCellValue(entry.getValue());
                    row.createCell(4).setCellValue(entry.getKey().getDefaultDays() - entry.getValue());
                    row.createCell(5).setCellValue(0); // TODO: Add carried forward days
                }
            }

            return writeWorkbookToBytes(workbook);
        } catch (IOException e) {
            log.error("Error generating leave balance report", e);
            throw new RuntimeException("Error generating leave balance report", e);
        }
    }

    @Override
    public byte[] generateLeaveUsageReport(Long departmentId, LocalDate startDate, LocalDate endDate) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leave Usage Report");
            createHeaderRow(sheet, "Employee", "Leave Type", "Start Date", "End Date", "Duration", "Status");

            List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                    departmentId, startDate, endDate, LeaveStatus.APPROVED);

            int rowNum = 1;
            for (LeaveRequest request : leaveRequests) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(request.getUser().getFullName());
                row.createCell(1).setCellValue(request.getLeaveType().getName());
                row.createCell(2).setCellValue(request.getStartDate().toString());
                row.createCell(3).setCellValue(request.getEndDate().toString());
                row.createCell(4).setCellValue(request.getEndDate().toEpochDay() - request.getStartDate().toEpochDay() + 1);
                row.createCell(5).setCellValue(request.getStatus().name());
            }

            return writeWorkbookToBytes(workbook);
        } catch (IOException e) {
            log.error("Error generating leave usage report", e);
            throw new RuntimeException("Error generating leave usage report", e);
        }
    }

    @Override
    public byte[] generateLeaveAccrualReport(Long departmentId, int year) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leave Accrual Report");
            createHeaderRow(sheet, "Employee", "Leave Type", "Accrual Rate", "Total Accrued", "Used", "Remaining");

            List<User> users = userRepository.findByDepartmentId(departmentId);
            int rowNum = 1;

            for (User user : users) {
                List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserAndStartDateBetween(
                        user, LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));

                Map<LeaveType, Long> leaveTypeCounts = leaveRequests.stream()
                        .collect(Collectors.groupingBy(
                                LeaveRequest::getLeaveType,
                                Collectors.summingLong(req -> req.getEndDate().toEpochDay() - req.getStartDate().toEpochDay() + 1)
                        ));

                for (Map.Entry<LeaveType, Long> entry : leaveTypeCounts.entrySet()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getFullName());
                    row.createCell(1).setCellValue(entry.getKey().getName());
                    row.createCell(2).setCellValue(entry.getKey().getAccrualRate());
                    row.createCell(3).setCellValue(entry.getKey().getDefaultDays());
                    row.createCell(4).setCellValue(entry.getValue());
                    row.createCell(5).setCellValue(entry.getKey().getDefaultDays() - entry.getValue());
                }
            }

            return writeWorkbookToBytes(workbook);
        } catch (IOException e) {
            log.error("Error generating leave accrual report", e);
            throw new RuntimeException("Error generating leave accrual report", e);
        }
    }

    @Override
    public byte[] generateLeaveCarryForwardReport(int year) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leave Carry Forward Report");
            createHeaderRow(sheet, "Employee", "Leave Type", "Previous Year Balance", "Carried Forward", "New Year Balance");

            // TODO: Implement carry forward report logic

            return writeWorkbookToBytes(workbook);
        } catch (IOException e) {
            log.error("Error generating leave carry forward report", e);
            throw new RuntimeException("Error generating leave carry forward report", e);
        }
    }

    @Override
    public byte[] generateLeaveTrendReport(Long departmentId, int startYear, int endYear) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leave Trend Report");
            createHeaderRow(sheet, "Year", "Leave Type", "Total Requests", "Average Duration", "Approval Rate");

            // TODO: Implement trend report logic

            return writeWorkbookToBytes(workbook);
        } catch (IOException e) {
            log.error("Error generating leave trend report", e);
            throw new RuntimeException("Error generating leave trend report", e);
        }
    }

    @Override
    public Map<LeaveType, Long> getLeaveTypeDistribution(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.APPROVED);

        return leaveRequests.stream()
                .collect(Collectors.groupingBy(
                        LeaveRequest::getLeaveType,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<User, Long> getTopLeaveUsers(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.APPROVED);

        return leaveRequests.stream()
                .collect(Collectors.groupingBy(
                        LeaveRequest::getUser,
                        Collectors.summingLong(req -> req.getEndDate().toEpochDay() - req.getStartDate().toEpochDay() + 1)
                ));
    }

    @Override
    public List<LeaveRequest> getPendingApprovalsReport(Long departmentId) {
        return leaveRequestRepository.findByUserDepartmentIdAndStatus(departmentId, LeaveStatus.PENDING);
    }

    @Override
    public List<LeaveRequest> getRejectedLeavesReport(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.REJECTED);
    }

    @Override
    public List<LeaveRequest> getCancelledLeavesReport(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByUserDepartmentIdAndStartDateBetweenAndStatus(
                departmentId, startDate, endDate, LeaveStatus.CANCELLED);
    }

    private void createHeaderRow(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
    }

    private byte[] writeWorkbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
} 