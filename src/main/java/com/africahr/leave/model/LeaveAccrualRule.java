package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leave_accrual_rules")
public class LeaveAccrualRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private BigDecimal accrualRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccrualFrequency accrualFrequency;

    @Column(nullable = false)
    private Integer maxCarryForward;

    @Column(nullable = false)
    private Integer expirationDays;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    public enum AccrualFrequency {
        MONTHLY,
        QUARTERLY,
        ANNUALLY
    }
} 