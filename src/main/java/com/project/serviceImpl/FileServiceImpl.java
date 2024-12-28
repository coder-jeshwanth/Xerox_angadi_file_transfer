package com.project.serviceImpl;

import com.project.controller.NotificationController;
import com.project.entity.UserFile;
import com.project.mapper.FileMapper;
import com.project.repository.UserFileRepository;
import com.project.requestDto.UserUploadRequestDto;
import com.project.responseDto.FileResponseDto;
import com.project.responseDto.UserUploadResponseDto;
import com.project.service.FileService;
import com.project.service.NotificationService;
import com.project.utility.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final UserFileRepository userFileRepository;
    private final FileStorageUtil fileStorageUtil;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    // Constructor injection to let Spring inject the required dependencies
    public FileServiceImpl(
            UserFileRepository userFileRepository,
            FileStorageUtil fileStorageUtil,
            NotificationService notificationService, // Inject NotificationService!
            SimpMessagingTemplate messagingTemplate
    ) {
        this.userFileRepository = userFileRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public UserUploadResponseDto uploadFile(UserUploadRequestDto requestDto) {
        try {
            String filePath = fileStorageUtil.saveFile(
                    requestDto.getUserName(),
                    requestDto.getUniqueFileName(),
                    requestDto.getFile().getBytes()
            );

            UserFile userFile = new UserFile();
            userFile.setUserName(requestDto.getUserName());
            userFile.setFileName(requestDto.getUniqueFileName());
            userFile.setFilePath(filePath);
            userFile.setUploadedAt(LocalDateTime.now());
            userFile.setPrinted(false);

            userFile = userFileRepository.save(userFile);

            return FileMapper.toResponseDto(userFile, "File uploaded successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + requestDto.getFile().getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileResponseDto> getUserFilesByUserName(String username) {
        List<UserFile> files = userFileRepository.findByUserName(username); // Add this repository method

        return files.stream()
                .map(file -> new FileResponseDto(
                        file.getId(),
                        file.getFileName(),
                        file.getFilePath(),
                        file.getUserName(),
                        file.isPrinted()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileResponseDto> getAllFiles() {
        List<UserFile> files = userFileRepository.findAll();

        return files.stream()
                .map(file -> new FileResponseDto(
                        file.getId(),
                        file.getFileName(),
                        file.getFilePath(),
                        file.getUserName(), // Use userName directly from UserFile
                        file.isPrinted()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean markFileAsPrinted(Long fileId) {

        // Retrieve the file entry from the database
        UserFile file = userFileRepository.findById(fileId).orElse(null);
        if (file == null) {
            return false; // File not found
        }

        // Delete the file from the file system
        File fileOnDisk = new File(file.getFilePath());
        if (fileOnDisk.exists() && fileOnDisk.delete()) {
            System.out.println("File deleted from the file system: " + file.getFilePath());

            // Check if the parent directory (user folder) is empty
            File parentFolder = fileOnDisk.getParentFile();
            if (parentFolder.exists() && parentFolder.isDirectory() && parentFolder.list().length == 0) {
                // Attempt to delete the folder
                if (parentFolder.delete()) {
                    System.out.println("User folder deleted: " + parentFolder.getAbsolutePath());
                } else {
                    System.err.println("Failed to delete the user folder: " + parentFolder.getAbsolutePath());
                }
            }

        } else {
            throw new RuntimeException("Failed to delete file from the file system: " + file.getFilePath());
        }

        // Remove the file metadata entry from the database
        userFileRepository.deleteById(fileId);

        return true;
    }
    @Override
    @Transactional(readOnly = true)
    public UserFile getFileById(Long fileId) {
        return userFileRepository.findById(fileId).orElse(null);
    }

}