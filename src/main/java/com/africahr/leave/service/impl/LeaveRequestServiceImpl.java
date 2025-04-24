package com.africahr.leave.service.impl;

import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.model.LeaveStatus;
import com.africahr.leave.model.User;
import com.africahr.leave.repository.LeaveRequestRepository;
import com.africahr.leave.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setCreatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(leaveRequest);
    }

    @Override
    @Transactional
    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequest) {
        LeaveRequest existingRequest = getLeaveRequestById(id);
        existingRequest.setLeaveType(leaveRequest.getLeaveType());
        existingRequest.setStartDate(leaveRequest.getStartDate());
        existingRequest.setEndDate(leaveRequest.getEndDate());
        existingRequest.setHalfDay(leaveRequest.isHalfDay());
        existingRequest.setReason(leaveRequest.getReason());
        existingRequest.setUpdatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(existingRequest);
    }

    @Override
    @Transactional
    public void deleteLeaveRequest(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    @Override
    public LeaveRequest getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByUser(User user) {
        return leaveRequestRepository.findByUser(user);
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByStatus(LeaveStatus status) {
        return leaveRequestRepository.findByStatus(status);
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByApprover(User approver) {
        return leaveRequestRepository.findByApproverAndStatus(approver, LeaveStatus.PENDING);
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByStartDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public LeaveRequest approveLeaveRequest(Long id, User approver, String comment) {
        LeaveRequest request = getLeaveRequestById(id);
        request.setStatus(LeaveStatus.APPROVED);
        request.setApprover(approver);
        request.setApproverComment(comment);
        request.setUpdatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(request);
    }

    @Override
    @Transactional
    public LeaveRequest rejectLeaveRequest(Long id, User approver, String comment) {
        LeaveRequest request = getLeaveRequestById(id);
        request.setStatus(LeaveStatus.REJECTED);
        request.setApprover(approver);
        request.setApproverComment(comment);
        request.setUpdatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(request);
    }

    @Override
    @Transactional
    public LeaveRequest cancelLeaveRequest(Long id, User user) {
        LeaveRequest request = getLeaveRequestById(id);
        if (!request.getUser().equals(user)) {
            throw new RuntimeException("Only the requester can cancel their leave request");
        }
        request.setStatus(LeaveStatus.CANCELLED);
        request.setUpdatedAt(LocalDateTime.now());
        return leaveRequestRepository.save(request);
    }
} 