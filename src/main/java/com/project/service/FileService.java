package com.project.service;

import com.project.entity.UserFile;
import com.project.requestDto.UserUploadRequestDto;
import com.project.responseDto.FileResponseDto;
import com.project.responseDto.UserUploadResponseDto;

import java.util.List;

public interface FileService {
    UserUploadResponseDto uploadFile(UserUploadRequestDto requestDto);


    boolean markFileAsPrinted(Long fileId);

    // Method to retrieve all stored files
    List<FileResponseDto> getAllFiles();

    List<FileResponseDto> getUserFilesByUserName(String username);

    UserFile getFileById(Long fileId);
}