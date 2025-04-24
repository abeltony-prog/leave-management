package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_accrual_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveAccrualRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "accrual_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal accrualRate; // e.g., 1.66 days per month

    @Column(name = "accrual_frequency", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccrualFrequency accrualFrequency;

    @Column(name = "max_carry_forward", nullable = false)
    private Integer maxCarryForward; // e.g., 5 days

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AccrualFrequency {
        MONTHLY,
        QUARTERLY,
        ANNUALLY
    }
} 