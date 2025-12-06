package com.sof3062.controller;

import com.sof3062.dto.ChatMessage;
import com.sof3062.service.UserSessionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * Controller to handle WebSocket messages and REST API requests for the chat
 * application.
 * <p>
 * This controller acts as the entry point for client interactions. It delegates
 * the management of user sessions to the {@link UserSessionService}, following
 * the Separation of Concerns principle.
 * </p>
 */
@Controller
public class ChatController {

    private final UserSessionService userSessionService;

    /**
     * Constructor injection for UserSessionService.
     * This promotes loose coupling and makes the controller easier to test.
     *
     * @param userSessionService The service managing user sessions.
     */
    public ChatController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    /**
     * REST API endpoint to get the list of currently online users.
     * <p>
     * This is used by the frontend to fetch the initial list of users
     * before a WebSocket connection is established or during polling.
     * </p>
     *
     * @return A Set of online usernames.
     */
    @GetMapping("/api/users")
    @ResponseBody
    public Set<String> getOnlineUsers() {
        return userSessionService.getOnlineUsers();
    }

    /**
     * Handles chat messages sent from clients.
     * <p>
     * Messages sent to "/app/chat.sendMessage" are routed here.
     * The method simply returns the message, which is then broadcast to
     * all subscribers of "/topic/public".
     * </p>
     *
     * @param chatMessage The message payload sent by the client.
     * @return The same message to be broadcast.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    /**
     * Handles new user join events.
     * <p>
     * When a client sends a message to "/app/username", this method is triggered.
     * It adds the user to the session service, stores the username in the WebSocket
     * session,
     * and broadcasts a JOIN message containing the updated list of online users.
     * </p>
     *
     * @param chatMessage    The message payload containing the sender's username.
     * @param headerAccessor Accessor to manipulate the WebSocket session
     *                       attributes.
     * @return A JOIN message containing the updated list of online users.
     */
    @MessageMapping("/username")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session so we can retrieve it on disconnect
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // Add user to the active list via service
        userSessionService.addUser(chatMessage.getSender());

        // Update the message content with the current list of users
        chatMessage.setContent(userSessionService.getOnlineUsers().toString());
        chatMessage.setType(ChatMessage.MessageType.JOIN);

        return chatMessage;
    }
}
