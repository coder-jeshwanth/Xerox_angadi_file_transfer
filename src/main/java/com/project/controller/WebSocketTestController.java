package com.project.controller;

import com.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class WebSocketTestController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notify")
    public void testNotify(@RequestParam String username) {
        // Send a test notification to the user
        notificationService.notifyUser(username, "Test notification from backend");
        System.out.println("Test notification sent!");
    }
}
