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
    private Long userId;
    private String userName;
    private Long leaveRequestId;
    private LocalDate date;
    private boolean isFullDay;
    private String description;
} 