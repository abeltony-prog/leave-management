package com.africahr.leave.task;

import com.africahr.leave.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileCleanupTask {
    private final DocumentRepository documentRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM every day
    public void cleanupOrphanedFiles() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                return;
            }

            // Get all file paths from database
            List<String> dbFilePaths = documentRepository.findAll().stream()
                    .map(doc -> Paths.get(doc.getFilePath()).getFileName().toString())
                    .toList();

            // Walk through upload directory
            Files.walk(uploadPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String fileName = file.getFileName().toString();
                        if (!dbFilePaths.contains(fileName)) {
                            try {
                                Files.delete(file);
                                log.info("Deleted orphaned file: {}", fileName);
                            } catch (IOException e) {
                                log.error("Failed to delete orphaned file: {}", fileName, e);
                            }
                        }
                    });
        } catch (IOException e) {
            log.error("Error during file cleanup", e);
        }
    }
} 