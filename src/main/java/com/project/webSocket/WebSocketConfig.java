package com.project.webSocket;

import com.project.component.PrincipalHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // Enable topic and queue destinations
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server messages
        config.setUserDestinationPrefix("/user"); // Prefix for user-specific messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // WebSocket handshake endpoint
                .setAllowedOrigins("http://localhost:3000") // Allow frontend origin
                .addInterceptors(new PrincipalHandshakeInterceptor()) //
                .withSockJS(); // Enable SockJS fallback
        System.out.println("WebSocket endpoints registered successfully.");
    }
}