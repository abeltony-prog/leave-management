package com.africahr.leave.service.impl;

import com.africahr.leave.dto.LeaveBalanceAdjustmentDto;
import com.africahr.leave.exception.ResourceNotFoundException;
import com.africahr.leave.model.*;
import com.africahr.leave.repository.LeaveAccrualRuleRepository;
import com.africahr.leave.repository.LeaveBalanceRepository;
import com.africahr.leave.repository.LeaveTypeRepository;
import com.africahr.leave.repository.UserRepository;
import com.africahr.leave.service.LeaveAccrualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveAccrualServiceImpl implements LeaveAccrualService {
    private final LeaveAccrualRuleRepository accrualRuleRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public void calculateAccruals(LocalDate calculationDate) {
        List<LeaveAccrualRule> activeRules = accrualRuleRepository.findByActiveTrue();
        List<User> activeUsers = userRepository.findByActiveTrue();

        for (User user : activeUsers) {
            for (LeaveAccrualRule rule : activeRules) {
                calculateAccrualForUserAndRule(user, rule, calculationDate);
            }
        }
    }

    @Override
    @Transactional
    public void calculateAccrualsForUser(User user, LocalDate calculationDate) {
        List<LeaveAccrualRule> activeRules = accrualRuleRepository.findByActiveTrue();
        for (LeaveAccrualRule rule : activeRules) {
            calculateAccrualForUserAndRule(user, rule, calculationDate);
        }
    }

    private void calculateAccrualForUserAndRule(User user, LeaveAccrualRule rule, LocalDate calculationDate) {
        LeaveBalance balance = getOrCreateLeaveBalance(user, rule.getLeaveType(), calculationDate.getYear());
        
        if (shouldAccrue(rule, calculationDate)) {
            BigDecimal accrualAmount = calculateAccrualAmount(rule, calculationDate);
            balance.setTotalDays(balance.getTotalDays().add(accrualAmount));
            leaveBalanceRepository.save(balance);
            log.info("Accrued {} days of {} leave for user {}", 
                    accrualAmount, rule.getLeaveType().getName(), user.getEmail());
        }
    }

    private boolean shouldAccrue(LeaveAccrualRule rule, LocalDate calculationDate) {
        return switch (rule.getAccrualFrequency()) {
            case MONTHLY -> calculationDate.getDayOfMonth() == 1;
            case QUARTERLY -> calculationDate.getDayOfMonth() == 1 && 
                    (calculationDate.getMonthValue() == 1 || 
                     calculationDate.getMonthValue() == 4 || 
                     calculationDate.getMonthValue() == 7 || 
                     calculationDate.getMonthValue() == 10);
            case ANNUALLY -> calculationDate.getDayOfMonth() == 1 && calculationDate.getMonthValue() == 1;
        };
    }

    private BigDecimal calculateAccrualAmount(LeaveAccrualRule rule, LocalDate calculationDate) {
        return switch (rule.getAccrualFrequency()) {
            case MONTHLY -> rule.getAccrualRate();
            case QUARTERLY -> rule.getAccrualRate().multiply(BigDecimal.valueOf(3));
            case ANNUALLY -> rule.getAccrualRate().multiply(BigDecimal.valueOf(12));
        };
    }

    @Override
    @Transactional
    public void applyCarryForward(User user, LeaveType leaveType, int year) {
        LeaveBalance currentYearBalance = getOrCreateLeaveBalance(user, leaveType, year);
        LeaveBalance previousYearBalance = leaveBalanceRepository
                .findByUserAndLeaveTypeAndYear(user, leaveType, year - 1)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Previous year balance not found for user " + user.getEmail()));

        LeaveAccrualRule rule = accrualRuleRepository.findByLeaveTypeAndActiveTrue(leaveType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Active accrual rule not found for leave type " + leaveType.getName()));

        BigDecimal remainingDays = previousYearBalance.getRemainingDays();
        BigDecimal carryForwardAmount = remainingDays.min(BigDecimal.valueOf(rule.getMaxCarryForward()));

        if (carryForwardAmount.compareTo(BigDecimal.ZERO) > 0) {
            currentYearBalance.setCarriedForwardDays(carryForwardAmount);
            currentYearBalance.setTotalDays(currentYearBalance.getTotalDays().add(carryForwardAmount));
            leaveBalanceRepository.save(currentYearBalance);
            log.info("Carried forward {} days of {} leave for user {}", 
                    carryForwardAmount, leaveType.getName(), user.getEmail());
        }
    }

    private LeaveBalance getOrCreateLeaveBalance(User user, LeaveType leaveType, int year) {
        return leaveBalanceRepository.findByUserAndLeaveTypeAndYear(user, leaveType, year)
                .orElseGet(() -> {
                    LeaveBalance newBalance = LeaveBalance.builder()
                            .user(user)
                            .leaveType(leaveType)
                            .year(year)
                            .totalDays(BigDecimal.ZERO)
                            .usedDays(BigDecimal.ZERO)
                            .remainingDays(BigDecimal.ZERO)
                            .carriedForwardDays(BigDecimal.ZERO)
                            .build();
                    return leaveBalanceRepository.save(newBalance);
                });
    }

    @Override
    public List<LeaveBalance> getLeaveBalances(User user) {
        return leaveBalanceRepository.findByUser(user);
    }

    @Override
    @Transactional
    public LeaveAccrualRule createAccrualRule(LeaveAccrualRule rule) {
        return accrualRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public LeaveAccrualRule updateAccrualRule(Long id, LeaveAccrualRule updatedRule) {
        LeaveAccrualRule existingRule = accrualRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accrual rule not found with id: " + id));

        existingRule.setAccrualRate(updatedRule.getAccrualRate());
        existingRule.setAccrualFrequency(updatedRule.getAccrualFrequency());
        existingRule.setMaxCarryForward(updatedRule.getMaxCarryForward());
        existingRule.setActive(updatedRule.isActive());

        return accrualRuleRepository.save(existingRule);
    }

    @Override
    @Transactional
    public void deactivateAccrualRule(Long id) {
        LeaveAccrualRule rule = accrualRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accrual rule not found with id: " + id));
        rule.setActive(false);
        accrualRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public LeaveBalance adjustLeaveBalance(LeaveBalanceAdjustmentDto adjustment) {
        User user = userRepository.findById(adjustment.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + adjustment.getUserId()));
        
        LeaveType leaveType = leaveTypeRepository.findById(adjustment.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + adjustment.getLeaveTypeId()));

        LeaveBalance balance = getOrCreateLeaveBalance(user, leaveType, adjustment.getYear());
        
        switch (adjustment.getAdjustmentType()) {
            case ADD -> balance.setTotalDays(balance.getTotalDays().add(adjustment.getAdjustmentAmount()));
            case SUBTRACT -> {
                BigDecimal newTotal = balance.getTotalDays().subtract(adjustment.getAdjustmentAmount());
                if (newTotal.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Cannot subtract more days than available");
                }
                balance.setTotalDays(newTotal);
            }
            case SET -> balance.setTotalDays(adjustment.getAdjustmentAmount());
        }

        log.info("Adjusted leave balance for user {}: {} {} days of {} leave for year {}. Reason: {}",
                user.getEmail(),
                adjustment.getAdjustmentType(),
                adjustment.getAdjustmentAmount(),
                leaveType.getName(),
                adjustment.getYear(),
                adjustment.getReason());

        return leaveBalanceRepository.save(balance);
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByDepartment(Long departmentId) {
        return leaveBalanceRepository.findByUserDepartmentId(departmentId);
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByYear(int year) {
        return leaveBalanceRepository.findByYear(year);
    }

    @Override
    public LeaveBalance getLeaveBalance(Long userId, Long leaveTypeId, int year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + leaveTypeId));

        return leaveBalanceRepository.findByUserAndLeaveTypeAndYear(user, leaveType, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leave balance not found for user " + userId + 
                        ", leave type " + leaveTypeId + 
                        ", year " + year));
    }
} 