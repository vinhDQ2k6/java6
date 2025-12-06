package com.sof3062.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Service responsible for managing active user sessions.
 * <p>
 * This class adheres to the Single Responsibility Principle (SRP) by handling
 * only the state of online users. It provides thread-safe methods to add,
 * remove, and retrieve connected users.
 * </p>
 */
@Service
public class UserSessionService {

    /**
     * A thread-safe Set to store unique usernames of currently connected users.
     * We use a synchronized set to prevent concurrency issues when multiple users
     * join or leave simultaneously.
     */
    private final Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

    /**
     * Adds a username to the list of online users.
     *
     * @param username The username of the user joining the chat.
     */
    public void addUser(String username) {
        onlineUsers.add(username);
    }

    /**
     * Removes a username from the list of online users.
     *
     * @param username The username of the user leaving the chat.
     */
    public void removeUser(String username) {
        onlineUsers.remove(username);
    }

    /**
     * Retrieves the current set of online users.
     *
     * @return A Set containing the usernames of all connected users.
     */
    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }
}
