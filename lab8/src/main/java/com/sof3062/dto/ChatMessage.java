package com.sof3062.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a chat message.
 * <p>
 * This class encapsulates the data exchanged between the client and server
 * during chat interactions. It uses Lombok annotations to reduce boilerplate
 * code.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    /**
     * The content of the message (e.g., "Hello World").
     * For JOIN/LEAVE messages, this may contain the list of online users.
     */
    private String content;

    /**
     * The username of the sender.
     */
    private String sender;

    /**
     * The type of the message.
     */
    private MessageType type;

    /**
     * Enum defining the types of messages supported by the application.
     */
    public enum MessageType {
        /**
         * A standard chat message.
         */
        CHAT,

        /**
         * A message indicating a user has joined.
         */
        JOIN,

        /**
         * A message indicating a user has left.
         */
        LEAVE
    }
}
