package com.project.component;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Component
public class PrincipalHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        // Log the start of the handshake
        System.out.println("Attempting WebSocket handshake...");

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession session = servletRequest.getServletRequest().getSession(false);

            if (session != null) {
                // Log the session ID for debugging
                System.out.println("HTTP session found. Session ID: " + session.getId());

                // Retrieve username and add it to attributes
                String username = (String) session.getAttribute("username");
                if (username != null) {
                    attributes.put("username", username);
                    System.out.println("Username set in WebSocket attributes: " + username);
                } else {
                    System.out.println("No 'username' attribute found in the HTTP session.");
                }
            } else {
                // Log the lack of session
                System.out.println("No HTTP session available for WebSocket handshake.");
            }
        }

        return true; // Proceed with the handshake
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("WebSocket handshake completed.");
    }
}