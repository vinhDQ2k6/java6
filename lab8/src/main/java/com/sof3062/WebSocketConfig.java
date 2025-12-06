package com.sof3062;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket and STOMP messaging.
 * <p>
 * This class enables the WebSocket message broker and configures the message
 * routing. It defines the endpoints for clients to connect to and the
 * prefixes for message destinations.
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints mapping each to a specific URL.
     * <p>
     * Clients will connect to this endpoint (e.g., http://localhost:8080/ws)
     * to establish the WebSocket connection. SockJS is enabled to provide
     * fallback options for browsers that don't support WebSocket natively.
     * </p>
     *
     * @param registry The registry to register endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    /**
     * Configures the message broker options.
     * <p>
     * - {@code enableSimpleBroker}: Enables a simple memory-based message broker
     * to carry messages back to the client on destinations prefixed with "/topic".
     * - {@code setApplicationDestinationPrefixes}: Defines the prefix for messages
     * that are bound for methods annotated with {@code @MessageMapping}.
     * </p>
     *
     * @param registry The registry to configure the broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
