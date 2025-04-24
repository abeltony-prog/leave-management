package com.africahr.leave.repository;

import com.africahr.leave.model.Document;
import com.africahr.leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByLeaveRequest(LeaveRequest leaveRequest);
    void deleteByLeaveRequest(LeaveRequest leaveRequest);
} 