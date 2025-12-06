package com.sof3062.listener;

import com.sof3062.dto.ChatMessage;
import com.sof3062.service.UserSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listener for WebSocket connection events.
 * <p>
 * This class listens for session disconnect events to handle user cleanup.
 * It uses {@link UserSessionService} to update the state and broadcasts
 * a LEAVE message to all connected clients.
 * </p>
 */
@Component
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserSessionService userSessionService;

    /**
     * Constructor injection for dependencies.
     *
     * @param messagingTemplate  Template for sending WebSocket messages.
     * @param userSessionService Service managing user sessions.
     */
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate,
            UserSessionService userSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.userSessionService = userSessionService;
    }

    /**
     * Handles WebSocket disconnect events.
     * <p>
     * When a user disconnects, this method retrieves their username from the
     * session,
     * removes them from the active user list, and broadcasts a LEAVE message
     * containing the updated list of online users.
     * </p>
     *
     * @param event The session disconnect event.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            userSessionService.removeUser(username);

            ChatMessage chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .content(userSessionService.getOnlineUsers().toString())
                    .build();

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
