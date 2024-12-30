package com.project.mapper;

import com.project.entity.UserFile;
import com.project.responseDto.UserUploadResponseDto;

public class FileMapper {

    public static UserUploadResponseDto toResponseDto(UserFile userFile, String uploadStatus) {
        return new UserUploadResponseDto(
                userFile.getUserName(),
                userFile.getFileName(),
                uploadStatus
        );
    }
}