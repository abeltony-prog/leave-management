package com.africahr.leave.service.impl;

import com.africahr.leave.exception.DocumentException;
import com.africahr.leave.exception.ResourceNotFoundException;
import com.africahr.leave.model.Document;
import com.africahr.leave.model.LeaveRequest;
import com.africahr.leave.repository.DocumentRepository;
import com.africahr.leave.service.DocumentService;
import com.africahr.leave.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final LeaveRequestService leaveRequestService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.allowed.file.types}")
    private String allowedFileTypes;

    @Override
    @Transactional
    public Document uploadDocument(MultipartFile file, Long leaveRequestId, String description) {
        try {
            // Validate file
            validateFile(file);
            
            // Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            
            // Get leave request and validate ownership
            LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId);
            if (!leaveRequest.getUser().getEmail().equals(currentUserEmail)) {
                throw new DocumentException("You are not authorized to upload documents for this leave request");
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // Create document record
            Document document = Document.builder()
                    .fileName(originalFilename)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(filePath.toString())
                    .leaveRequest(leaveRequest)
                    .description(description)
                    .build();
            
            return documentRepository.save(document);
        } catch (IOException e) {
            throw new DocumentException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new DocumentException("File is empty");
        }
        
        if (!Arrays.asList(allowedFileTypes.split(",")).contains(file.getContentType())) {
            throw new DocumentException("File type not allowed. Allowed types: " + allowedFileTypes);
        }
    }

    @Override
    public Document getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        
        // Check if user has access to this document
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        if (!document.getLeaveRequest().getUser().getEmail().equals(currentUserEmail)) {
            throw new DocumentException("You are not authorized to access this document");
        }
        
        return document;
    }

    @Override
    public List<Document> getDocumentsByLeaveRequest(Long leaveRequestId) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        // Get leave request and validate ownership
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId);
        if (!leaveRequest.getUser().getEmail().equals(currentUserEmail)) {
            throw new DocumentException("You are not authorized to view documents for this leave request");
        }
        
        return documentRepository.findByLeaveRequestId(leaveRequestId);
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        Document document = getDocumentById(id);
        
        // Check if user has permission to delete
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        if (!document.getLeaveRequest().getUser().getEmail().equals(currentUserEmail)) {
            throw new DocumentException("You are not authorized to delete this document");
        }
        
        try {
            // Delete file from storage
            Files.deleteIfExists(Paths.get(document.getFilePath()));
            // Delete record from database
            documentRepository.delete(document);
        } catch (IOException e) {
            throw new DocumentException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] downloadDocument(Long id) {
        Document document = getDocumentById(id);
        try {
            return Files.readAllBytes(Paths.get(document.getFilePath()));
        } catch (IOException e) {
            throw new DocumentException("Failed to download file: " + e.getMessage(), e);
        }
    }
} 