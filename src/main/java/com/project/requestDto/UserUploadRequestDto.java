package com.project.requestDto;

import org.springframework.web.multipart.MultipartFile;

public class UserUploadRequestDto {
    private String userName;
    private MultipartFile file;
    private String uniqueFileName; // Add this field

    // Default Constructor
    public UserUploadRequestDto() {
    }

    // Parameterized Constructor
    public UserUploadRequestDto(String userName, MultipartFile file, String uniqueFileName) {
        this.userName = userName;
        this.file = file;
        this.uniqueFileName = uniqueFileName;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }

    public void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }
}