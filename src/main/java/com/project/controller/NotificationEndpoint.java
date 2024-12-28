package com.project.controller;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{username}")
public class NotificationEndpoint {
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        System.out.println("WebSocket connection opened for user: " + username);

        // Ensure the username is valid before adding to userSessions
        if (username == null || username.isEmpty()) {
            System.err.println("Invalid username provided. Closing session...");
            try {
                session.close();
                System.err.println("WebSocket session closed due to invalid username.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Add to session map
        userSessions.put(username, session);
        System.out.println("Active WebSocket sessions: " + userSessions.keySet());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message from client: " + message);
        // Add more logging if you implement specific message handling logic
    }

    @OnClose
    public void onClose(@PathParam("username") String username, Session session) {
        userSessions.remove(username);
        System.out.println("WebSocket connection closed for user: " + username);
        System.out.println("Updated active WebSocket sessions: " + userSessions.keySet());
    }

    public static void notifyUser(String username, String message) {
        System.out.println("Preparing to send WebSocket message to user: " + username);

        try {
            Session session = userSessions.get(username);
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
                System.out.println("Message sent to user: " + username + " - " + message);
            } else {
                System.err.println("No active WebSocket session found for user: " + username);
            }
        } catch (IOException e) {
            System.err.println("Error while sending message to user: " + username);
            e.printStackTrace();
        }
    }
}
