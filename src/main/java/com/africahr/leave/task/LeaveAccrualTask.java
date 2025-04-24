package com.africahr.leave.task;

import com.africahr.leave.service.LeaveAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaveAccrualTask {
    private final LeaveAccrualService leaveAccrualService;

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the first day of each month
    public void calculateMonthlyAccruals() {
        log.info("Starting monthly leave accrual calculation");
        leaveAccrualService.calculateAccruals(LocalDate.now());
        log.info("Completed monthly leave accrual calculation");
    }

    @Scheduled(cron = "0 0 0 1 1 ?") // Run at midnight on January 1st
    public void applyAnnualCarryForward() {
        log.info("Starting annual leave carry forward calculation");
        int currentYear = LocalDate.now().getYear();
        leaveAccrualService.calculateAccruals(LocalDate.of(currentYear, 1, 1));
        log.info("Completed annual leave carry forward calculation");
    }
} 