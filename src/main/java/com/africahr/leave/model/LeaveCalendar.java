package com.africahr.leave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "leave_calendar")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_request_id")
    private LeaveRequest leaveRequest;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean isFullDay;

    @Column
    private String description;
} 