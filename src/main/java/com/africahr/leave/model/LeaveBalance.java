package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "leave_balances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private double totalDays;
    
    @Column(nullable = false)
    private double usedDays;
    
    @Column(nullable = false)
    private double remainingDays;
    
    @Column
    private double carriedForwardDays;
    
    @Column(nullable = false)
    private int year;
    
    @Column(nullable = false)
    private LocalDate lastUpdated;
} 