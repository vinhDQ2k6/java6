# Lab 8: WebSocket Chat Application - Step-by-Step Guide

This guide documents the complete process of building a Real-Time Chat Application using **Spring Boot (Backend)** and **Vue.js (Frontend)**. It covers everything from initial setup to the final refactored version using SOLID principles.

---

## 1. Project Overview

We built a chat application where:

-   Users can join with a username.
-   Users can send public messages to everyone.
-   Users can see a list of currently online users.
-   The list updates automatically when someone joins or leaves.

**Technologies used:**

-   **Java 17/21** & **Spring Boot 3**: Backend server.
-   **WebSocket & STOMP**: Real-time communication protocol.
-   **SockJS**: Fallback for browsers that don't support WebSockets.
-   **Vue.js 2**: Frontend framework (using `http-vue-loader` for simplicity).
-   **Bootstrap 4**: Styling.

---

## 2. Backend Implementation (Spring Boot)

### Step 2.1: Dependencies (`pom.xml`)

We added the `spring-boot-starter-websocket` dependency to enable WebSocket capabilities.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### Step 2.2: WebSocket Configuration (`WebSocketConfig.java`)

We created a configuration class to enable the message broker.

-   **`@EnableWebSocketMessageBroker`**: Enables WebSocket handling.
-   **`registerStompEndpoints`**: Defined `/ws` as the endpoint where clients connect. We enabled **SockJS** here to support older browsers.
-   **`configureMessageBroker`**:
    -   `/app`: Prefix for messages sent **from client to server**.
    -   `/topic`: Prefix for messages sent **from server to client** (broadcast).

### Step 2.3: Data Model (`ChatMessage.java`)

We created a DTO (Data Transfer Object) to define the structure of messages exchanged.

-   **Fields**: `content`, `sender`, `type` (CHAT, JOIN, LEAVE).
-   **Lombok**: Used `@Data`, `@Builder` to reduce boilerplate code.

### Step 2.4: Service Layer (`UserSessionService.java`) - _SOLID Principle_

To follow the **Single Responsibility Principle (SRP)**, we moved the logic of managing online users out of the Controller.

-   **Responsibility**: Stores the list of active usernames in a thread-safe `Set`.
-   **Methods**: `addUser`, `removeUser`, `getOnlineUsers`.

### Step 2.5: Controller (`ChatController.java`)

Handles incoming WebSocket messages and REST requests.

-   **`@MessageMapping("/chat.sendMessage")`**: Receives chat messages and forwards them to `/topic/public`.
-   **`@MessageMapping("/username")`**: Handles user joins. It adds the user to the session service and broadcasts a JOIN message with the updated user list.
-   **`@GetMapping("/api/users")`**: A REST endpoint to let the frontend fetch the online user list _before_ connecting via WebSocket.

### Step 2.6: Event Listener (`WebSocketEventListener.java`)

Handles disconnection events automatically.

-   Listens for `SessionDisconnectEvent`.
-   Removes the user from `UserSessionService`.
-   Broadcasts a `LEAVE` message to `/topic/public` so other clients know someone left.

### Step 2.7: Security (`SecurityConfig.java`)

Since Spring Security is included in the parent project, it defaults to requiring login.

-   We created a config to **permit all requests** (`.anyRequest().permitAll()`).
-   We disabled **Frame Options** to allow SockJS to work (it sometimes uses iframes).

---

## 3. Frontend Implementation (Vue.js)

We used a simple setup without Node.js build tools (like Webpack/Vite) to keep it easy for freshers.

### Step 3.1: Entry Point (`index.html`)

-   Loads libraries via CDN: **Vue.js**, **SockJS**, **Stomp.js**, **Bootstrap**.
-   Uses `http-vue-loader` to load `.vue` files directly in the browser.
-   Mounts the `<chat-view>` component.

### Step 3.2: Chat Component (`ChatView.vue`)

This file contains the HTML (Template), CSS (Style), and Logic (Script).

**Key Logic Flow:**

1.  **Mounting**: When the page loads, it calls `/api/users` to show who is online immediately. It polls this API every 2 seconds until connected.
2.  **Connecting**:
    -   User enters username and clicks "Connect".
    -   Creates a `SockJS` connection to `http://localhost:8080/ws`.
    -   Creates a `Stomp` client over that socket.
3.  **Subscribing**:
    -   Once connected, it subscribes to `/topic/public`.
    -   Any message sent to this topic by the server will trigger `onMessageReceived`.
4.  **Sending**:
    -   User types a message and hits Enter.
    -   Client sends JSON to `/app/chat.sendMessage`.
5.  **Receiving**:
    -   When a message arrives, it's pushed to the `messages` array.
    -   If it's a JOIN/LEAVE message, the `onlineUsers` list is updated.

---

## 4. How to Run

1.  **Start the Backend**:
    Open a terminal in the `lab8` folder and run:

    ```bash
    ./mvnw spring-boot:run
    ```

    (Or run `Main.java` in VS Code).

2.  **Access the Application**:
    Open your browser to:
    [http://localhost:8080/index.html](http://localhost:8080/index.html)

3.  **Test**:
    -   Open multiple tabs.
    -   Enter different usernames and connect.
    -   Chat and watch the "Online Users" list update in real-time.

---

## 5. Key Concepts for Freshers

-   **WebSocket**: A protocol providing full-duplex communication channels over a single TCP connection. Unlike HTTP (request-response), the server can send data to the client anytime.
-   **STOMP (Simple Text Oriented Messaging Protocol)**: A sub-protocol used on top of WebSocket. It defines how to send messages (like "SEND", "SUBSCRIBE") so we don't have to parse raw strings manually.
-   **Pub/Sub (Publish/Subscribe)**: The pattern used here. Clients "Subscribe" to a topic (e.g., `/topic/public`). When anyone "Publishes" a message to that topic, everyone subscribed receives it.
-   **SOLID Principles**:
    -   **SRP**: We separated user session logic into its own Service class.
    -   **DI**: We injected dependencies via constructors instead of creating them manually.

---

_Created by GitHub Copilot_
