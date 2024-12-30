package com.project.responseDto;

public class UserUploadResponseDto {
    private String userName;
    private String fileName;
    private String uploadStatus;

    // Default Constructor
    public UserUploadResponseDto() {
    }

    // Parameterized Constructor
    public UserUploadResponseDto(String userName, String fileName, String uploadStatus) {
        this.userName = userName;
        this.fileName = fileName;
        this.uploadStatus = uploadStatus;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}