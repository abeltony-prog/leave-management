package com.africahr.leave.service.impl;

import com.africahr.leave.model.LeaveBalance;
import com.africahr.leave.model.LeaveType;
import com.africahr.leave.model.User;
import com.africahr.leave.repository.LeaveBalanceRepository;
import com.africahr.leave.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Override
    @Transactional
    public LeaveBalance createLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalance.setLastUpdated(LocalDate.now());
        return leaveBalanceRepository.save(leaveBalance);
    }

    @Override
    @Transactional
    public LeaveBalance updateLeaveBalance(Long id, LeaveBalance leaveBalance) {
        LeaveBalance existingBalance = getLeaveBalanceById(id);
        existingBalance.setTotalDays(leaveBalance.getTotalDays());
        existingBalance.setUsedDays(leaveBalance.getUsedDays());
        existingBalance.setRemainingDays(leaveBalance.getRemainingDays());
        existingBalance.setCarriedForwardDays(leaveBalance.getCarriedForwardDays());
        existingBalance.setLastUpdated(LocalDate.now());
        return leaveBalanceRepository.save(existingBalance);
    }

    @Override
    @Transactional
    public void deleteLeaveBalance(Long id) {
        leaveBalanceRepository.deleteById(id);
    }

    @Override
    public LeaveBalance getLeaveBalanceById(Long id) {
        return leaveBalanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave balance not found with id: " + id));
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByUser(User user) {
        return leaveBalanceRepository.findByUser(user);
    }

    @Override
    public LeaveBalance getLeaveBalanceByUserAndTypeAndYear(User user, LeaveType leaveType, int year) {
        return leaveBalanceRepository.findByUserAndLeaveTypeAndYear(user, leaveType, year)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Leave balance not found for user %d and leave type %s in year %d",
                                user.getId(), leaveType.getName(), year)));
    }

    @Override
    public List<LeaveBalance> getLeaveBalancesByUserAndYear(User user, int year) {
        return leaveBalanceRepository.findByUserAndYear(user, year);
    }

    @Override
    @Transactional
    public LeaveBalance adjustLeaveBalance(Long id, double adjustment) {
        LeaveBalance balance = getLeaveBalanceById(id);
        double newUsedDays = balance.getUsedDays() + adjustment;
        double newRemainingDays = balance.getTotalDays() - newUsedDays;
        
        if (newRemainingDays < 0) {
            throw new RuntimeException("Insufficient leave balance");
        }
        
        balance.setUsedDays(newUsedDays);
        balance.setRemainingDays(newRemainingDays);
        balance.setLastUpdated(LocalDate.now());
        
        return leaveBalanceRepository.save(balance);
    }
} 