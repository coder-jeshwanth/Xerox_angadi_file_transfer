package com.project.serviceImpl;

import com.project.controller.NotificationController;
import com.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyUser(String userName, String message) {
        System.out.println("Notifying user: " + userName + " with message: " + message);
        // Send WebSocket notification
        messagingTemplate.convertAndSendToUser(userName, "/queue/notifications", message);

        // Log the notification
        System.out.println("Notification sent to user [" + userName + "]: " + message);
    }
}
