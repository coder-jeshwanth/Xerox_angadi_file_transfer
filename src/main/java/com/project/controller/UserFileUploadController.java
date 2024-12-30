package com.project.controller;

import com.project.requestDto.UserUploadRequestDto;
import com.project.responseDto.FileResponseDto;
import com.project.responseDto.UserUploadResponseDto;
import com.project.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserFileUploadController {

    private final FileService fileService;

    public UserFileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<UserUploadResponseDto>> uploadFiles(
            @RequestParam("username") String userName,
            @RequestParam("files") MultipartFile[] files
    ) {
        List<UserUploadResponseDto> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Extract the original file name
                String originalFileName = file.getOriginalFilename();

                if (originalFileName == null || originalFileName.isBlank()) {
                    throw new RuntimeException("Invalid file name");
                }

                // Pass the original file name along with the user and file to DTO
                UserUploadRequestDto requestDto = new UserUploadRequestDto(userName, file, originalFileName);

                // Process the upload via the service
                UserUploadResponseDto responseDto = fileService.uploadFile(requestDto);

                // Add response to the list
                responseList.add(responseDto);

            } catch (Exception e) {
                // In case of an error, create an error response for this file
                responseList.add(new UserUploadResponseDto(userName, file.getOriginalFilename(), "Upload failed: " + e.getMessage()));
            }
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/dashboard/{username}")
    public ResponseEntity<List<FileResponseDto>> getUserDashboard(@PathVariable String username) {
        List<FileResponseDto> userFiles = fileService.getUserFilesByUserName(username);
        return ResponseEntity.ok(userFiles);
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<Object> checkIfUserNameExists(@RequestParam("username") String username) {
        try {
            // Call the service to get all existing usernames
            List<String> existingUserNames = fileService.getAllUserNames();

            // Check if the provided username already exists
            if (existingUserNames.contains(username)) {
                return ResponseEntity.badRequest().body(new Object() {
                    public final String status = "error";
                    public final String message = "User name already exists";
                });
            }

            // If the name does not exist, return a success response
            return ResponseEntity.ok(new Object() {
                public final String status = "success";
                public final String message = "User name is available";
            });
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Object() {
                public final String status = "error";
                public final String message = "An error occurred while checking the user name: " + e.getMessage();
            });
        }
    }
}