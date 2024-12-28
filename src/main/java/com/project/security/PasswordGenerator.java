package com.project.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "5050"; // Replace with your raw password
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword); // Use this hashed password in the database
    }
}
