package com.africahr.leave.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceAdjustmentDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Leave type ID is required")
    private Long leaveTypeId;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Adjustment amount is required")
    @PositiveOrZero(message = "Adjustment amount must be positive or zero")
    private BigDecimal adjustmentAmount;

    private String reason;

    @NotNull(message = "Adjustment type is required")
    private AdjustmentType adjustmentType;

    public enum AdjustmentType {
        ADD,
        SUBTRACT,
        SET
    }
} 