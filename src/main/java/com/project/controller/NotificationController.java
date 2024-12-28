package com.project.controller;

import com.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/test")
    public void sendTestNotification(@RequestParam String userName) {
        System.out.println("Received request to send notification to user: " + userName);

        try {
            notificationService.notifyUser(userName, "Test notification using WebSocket!");
            System.out.println("Notification successfully sent to user: " + userName);
        } catch (Exception e) {
            System.err.println("Failed to send notification to user: " + userName);
            e.printStackTrace(); // Log the full stack trace for debugging
        }
    }
}