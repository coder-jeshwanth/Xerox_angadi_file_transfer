package com.project.controller;

import com.project.entity.Owner;
import com.project.entity.UserFile;
import com.project.repository.OwnerRepository;
import com.project.requestDto.OwnerLoginRequestDto;
import com.project.responseDto.FileResponseDto;
import com.project.responseDto.OwnerLoginResponseDto;
import com.project.security.JwtUtils;
import com.project.service.FileService;
import com.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class OwnerController {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final FileService fileService;

    @Autowired
    private NotificationService notificationService;

    public OwnerController(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, FileService fileService) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.fileService = fileService;
    }

    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<OwnerLoginResponseDto> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        Owner owner = ownerRepository.findByUsername(username);
        if (owner == null || !passwordEncoder.matches(password, owner.getPassword())) {
            return ResponseEntity.status(401).body(new OwnerLoginResponseDto("Invalid username or password", null));
        }

        String token = jwtUtils.generateToken(owner.getUsername());
        return ResponseEntity.ok(new OwnerLoginResponseDto("Login successful", token));
    }

    /**
     * Mark a file as printed and delete it from storage.
     *
     * @param id The ID of the file to print
     * @return Response with success or error message
     */
    @PostMapping("/print/{id}")
    public ResponseEntity<?> printFile(@PathVariable Long id) {
        try {
            // Fetch file details by ID
            var file = fileService.getFileById(id);
            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("File not found", "The file with ID " + id + " does not exist."));
            }

            // Check if the file is already printed
            if (file.isPrinted()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("File already printed", "The file with ID " + id + " has already been processed."));
            }

            // Mark the file as printed and delete it
            boolean fileExists = fileService.markFileAsPrinted(id);
            if (!fileExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("File not found", "The file with ID " + id + " does not exist."));
            }

            // Retrieve username from the file metadata
            String username = file.getUserName();
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createErrorResponse("User not found", "The file does not belong to any user."));
            }

            // Notify the specific user
            notificationService.notifyUser(username, "File with ID " + id + " has been printed and deleted successfully!");

            return ResponseEntity.ok(createSuccessResponse(
                    "File printed and deleted successfully!",
                    "The file with ID " + id + " has been processed."
            ));
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error printing file", e.getMessage()));
        }
    }

    /**
     * Display all files and their details.
     *
     * @return Response with all files or an error
     */
    @GetMapping("/files")
    public ResponseEntity<?> getAllFilesGroupedByUsername(
            @RequestParam(value = "fetchFile", required = false) Boolean fetchFile,
            @RequestParam(value = "id", required = false) Long fileId
    ) {
        try {
            // If fetchFile is true, serve the file content
            if (Boolean.TRUE.equals(fetchFile)) {
                if (fileId == null) {
                    return ResponseEntity.badRequest().body(createErrorResponse("Missing file ID", "File ID is required to fetch file content."));
                }

                // Fetch the file by ID
                UserFile file = fileService.getFileById(fileId);
                if (file == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(createErrorResponse("File not found", "The file with ID " + fileId + " does not exist."));
                }

                // Check if the file exists on the disk
                File fileOnDisk = new File(file.getFilePath());
                if (!fileOnDisk.exists()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(createErrorResponse("File not found on disk", "The file with ID " + fileId + " could not be located on the server."));
                }

                // Serve the file content
                var resource = new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(fileOnDisk));
                return ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                        .contentType(getMediaType(file.getFileName()))
                        .contentLength(fileOnDisk.length())
                        .body(resource);
            }

            // If fetchFile is not true, serve metadata grouped by username
            var files = fileService.getAllFiles();

            if (files.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("No files found", "There are no files currently stored."));
            }

            // Group files by username
            Map<String, List<FileResponseDto>> groupedFiles = files.stream()
                    .collect(Collectors.groupingBy(FileResponseDto::getUsername));

            return ResponseEntity.ok(new Object() {
                public final String status = "success";
                public final String message = "Files retrieved successfully";
                public final Map<String, List<FileResponseDto>> data = groupedFiles;
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving files", e.getMessage()));
        }
    }

    // Helper method to determine the file MediaType
    private org.springframework.http.MediaType getMediaType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return org.springframework.http.MediaType.APPLICATION_PDF;
            case "png":
            case "jpg":
            case "jpeg":
                return org.springframework.http.MediaType.IMAGE_JPEG;
            case "gif":
                return org.springframework.http.MediaType.IMAGE_GIF;
            case "txt":
                return org.springframework.http.MediaType.TEXT_PLAIN;
            default:
                return org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private static Object createSuccessResponse(String message, String details) {
        return new Object() {
            public final String status = "success";
            public final String messageSummary = message;
            public final String messageDetails = details;
        };
    }

    private static Object createErrorResponse(String message, String details) {
        return new Object() {
            public final String status = "error";
            public final String messageSummary = message;
            public final String messageDetails = details;
        };
    }
}