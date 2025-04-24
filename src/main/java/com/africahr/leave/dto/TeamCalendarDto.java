package com.africahr.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamCalendarDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String leaveType;
    private String employeeName;
    private String department;
    private boolean isHalfDay;
    private String reason;
} 