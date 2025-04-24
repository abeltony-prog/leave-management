package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "max_days", nullable = false)
    private Integer maxDays;

    @Column(name = "default_days", nullable = false)
    private Integer defaultDays;

    @Column(name = "accrual_rate", nullable = false)
    private Double accrualRate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Integer getDefaultDays() {
        return defaultDays;
    }

    public Double getAccrualRate() {
        return accrualRate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 