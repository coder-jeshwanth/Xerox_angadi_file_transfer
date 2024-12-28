package com.project.responseDto;

public class FileResponseDto {
    private Long id;
    private String fileName;
    private String filePath;
    private String username;       // Includes username
    private boolean isPrinted;

    // Constructor
    public FileResponseDto(Long id, String fileName, String filePath, String username, boolean isPrinted) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.username = username;
        this.isPrinted = isPrinted;
    }

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPrinted() {
        return isPrinted;
    }

    public void setPrinted(boolean printed) {
        isPrinted = printed;
    }
}