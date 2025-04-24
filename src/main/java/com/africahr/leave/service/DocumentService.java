package com.africahr.leave.service;

import com.africahr.leave.model.Document;
import com.africahr.leave.model.LeaveRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    Document uploadDocument(MultipartFile file, Long leaveRequestId, String description);
    Document getDocumentById(Long id);
    List<Document> getDocumentsByLeaveRequest(Long leaveRequestId);
    void deleteDocument(Long id);
    byte[] downloadDocument(Long id);
} 