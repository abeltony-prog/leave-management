package com.africahr.leave.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Leave type ID is required")
    private Long leaveTypeId;

    @NotNull(message = "Total days is required")
    @Positive(message = "Total days must be positive")
    private double totalDays;

    @NotNull(message = "Used days is required")
    @PositiveOrZero(message = "Used days cannot be negative")
    private double usedDays;

    @NotNull(message = "Remaining days is required")
    @PositiveOrZero(message = "Remaining days cannot be negative")
    private double remainingDays;

    @PositiveOrZero(message = "Carried forward days cannot be negative")
    private double carriedForwardDays;

    @NotNull(message = "Year is required")
    private int year;

    private LocalDate lastUpdated;
} 