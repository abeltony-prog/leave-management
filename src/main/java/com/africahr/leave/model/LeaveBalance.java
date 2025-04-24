package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_balances")
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private BigDecimal totalDays;

    @Column(nullable = false)
    private BigDecimal usedDays;

    @Column(nullable = false)
    private BigDecimal remainingDays;

    @Column(nullable = false)
    private BigDecimal carriedForwardDays;

    @Column(nullable = false)
    private Integer year;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
        this.remainingDays = this.totalDays.subtract(this.usedDays).add(this.carriedForwardDays);
    }
} 