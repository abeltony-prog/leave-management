package com.africahr.leave.service.impl;

import com.africahr.leave.model.LeaveType;
import com.africahr.leave.repository.LeaveTypeRepository;
import com.africahr.leave.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveType createLeaveType(LeaveType leaveType) {
        leaveType.setCreatedAt(LocalDateTime.now());
        leaveType.setUpdatedAt(LocalDateTime.now());
        return leaveTypeRepository.save(leaveType);
    }

    @Override
    @Transactional
    public LeaveType updateLeaveType(Long id, LeaveType leaveType) {
        LeaveType existingType = getLeaveTypeById(id);
        existingType.setName(leaveType.getName());
        existingType.setDescription(leaveType.getDescription());
        existingType.setMaxDays(leaveType.getMaxDays());
        existingType.setUpdatedAt(LocalDateTime.now());
        return leaveTypeRepository.save(existingType);
    }

    @Override
    @Transactional
    public void deleteLeaveType(Long id) {
        leaveTypeRepository.deleteById(id);
    }

    @Override
    public LeaveType getLeaveTypeById(Long id) {
        return leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found with id: " + id));
    }

    @Override
    public LeaveType getLeaveTypeByName(String name) {
        return leaveTypeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Leave type not found with name: " + name));
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
} 