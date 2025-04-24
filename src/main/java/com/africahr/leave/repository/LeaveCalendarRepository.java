package com.africahr.leave.repository;

import com.africahr.leave.model.LeaveCalendar;
import com.africahr.leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveCalendarRepository extends JpaRepository<LeaveCalendar, Long> {
    List<LeaveCalendar> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<LeaveCalendar> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<LeaveCalendar> findByLeaveRequestAndDateBetween(LeaveRequest leaveRequest, LocalDate startDate, LocalDate endDate);
} 