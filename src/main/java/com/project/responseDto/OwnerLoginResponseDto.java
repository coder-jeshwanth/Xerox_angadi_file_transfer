package com.project.responseDto;

public class OwnerLoginResponseDto {
    private String message;
    private String jwtToken;

    // Default Constructor
    public OwnerLoginResponseDto() {
    }

    // Parameterized Constructor
    public OwnerLoginResponseDto(String message, String jwtToken) {
        this.message = message;
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}