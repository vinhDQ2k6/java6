<template>
	<div class="chat-container">
		<h2 class="text-center mb-4">WebSocket Chat</h2>

		<div class="row">
			<!-- Online Users Sidebar -->
			<div class="col-md-4">
				<div class="card mb-3">
					<div class="card-header bg-info text-white">Online Users</div>
					<ul class="list-group list-group-flush">
						<li class="list-group-item" v-for="user in onlineUsers" :key="user">
							<span class="badge badge-success mr-2">●</span> {{ user }}
						</li>
						<li class="list-group-item text-muted" v-if="onlineUsers.length === 0">No users online</li>
					</ul>
				</div>
			</div>

			<!-- Chat Area -->
			<div class="col-md-8">
				<!-- Connection Controls -->
				<div class="card mb-3">
					<div class="card-body">
						<div class="form-group">
							<label>Username:</label>
							<input type="text" v-model="username" class="form-control" :disabled="connected" />
						</div>
						<div class="btn-group w-100">
							<button @click="connect" class="btn btn-success" :disabled="connected || !username">
								Connect
							</button>
							<button @click="disconnect" class="btn btn-danger" :disabled="!connected">
								Disconnect
							</button>
						</div>
					</div>
				</div>

				<div class="card">
					<div class="card-body">
						<div class="message-area" ref="messageArea">
							<div v-for="(msg, index) in messages" :key="index">
								<div v-if="msg.type === 'JOIN'" class="event-message">{{ msg.sender }} joined!</div>
								<div v-else-if="msg.type === 'LEAVE'" class="event-message">{{ msg.sender }} left!</div>
								<div v-else class="chat-message">
									<strong>{{ msg.sender }}:</strong> {{ msg.content }}
								</div>
							</div>
						</div>

						<div class="input-group mt-3">
							<input
								type="text"
								v-model="messageContent"
								class="form-control"
								placeholder="Type a message..."
								@keyup.enter="sendMessage"
								:disabled="!connected"
							/>
							<div class="input-group-append">
								<button @click="sendMessage" class="btn btn-primary" :disabled="!connected">
									Send
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	/**
	 * ChatView Component
	 *
	 * This Vue component handles the entire chat interface and logic.
	 * It manages the WebSocket connection, message sending/receiving,
	 * and the list of online users.
	 */
	module.exports = {
		data: function () {
			return {
				username: "", // Current user's name
				messageContent: "", // Content of the message being typed
				stompClient: null, // The STOMP client instance
				connected: false, // Connection status
				messages: [], // List of chat messages
				onlineUsers: [], // List of currently online users
				polling: null // Interval ID for polling
			};
		},
		mounted() {
			// Fetch the initial list of users when the component loads
			this.fetchOnlineUsers();

			// Poll for updates every 2 seconds if not connected
			// This ensures the user sees who is online before they even join
			this.polling = setInterval(() => {
				if (!this.connected) {
					this.fetchOnlineUsers();
				}
			}, 2000);
		},
		beforeDestroy() {
			// Clean up the polling interval when the component is destroyed
			clearInterval(this.polling);
		},
		methods: {
			/**
			 * Fetches the list of online users via REST API.
			 */
			fetchOnlineUsers() {
				fetch("/api/users")
					.then((response) => response.json())
					.then((data) => {
						// Only update if not connected (connected users get updates via websocket)
						if (!this.connected) {
							this.onlineUsers = data;
						}
					})
					.catch((err) => console.error("Error fetching users:", err));
			},
			/**
			 * Establishes a WebSocket connection.
			 */
			connect() {
				if (this.username) {
					// Connect to the /ws endpoint defined in WebSocketConfig
					var socket = new SockJS("/ws");
					this.stompClient = Stomp.over(socket);
					this.stompClient.connect({}, this.onConnected, this.onError);
				}
			},
			/**
			 * Disconnects the WebSocket connection.
			 */
			disconnect() {
				if (this.stompClient) {
					this.stompClient.disconnect();
				}
				this.connected = false;
				this.onlineUsers = [];
				console.log("Disconnected");
			},
			/**
			 * Callback for successful connection.
			 */
			onConnected() {
				this.connected = true;

				// Subscribe to the public topic to receive messages
				this.stompClient.subscribe("/topic/public", this.onMessageReceived);

				// Send a JOIN message to the server to announce presence
				this.stompClient.send("/app/username", {}, JSON.stringify({ sender: this.username, type: "JOIN" }));
			},
			/**
			 * Callback for connection errors.
			 */
			onError(error) {
				console.log("Could not connect to WebSocket server. Please refresh this page to try again!");
				this.connected = false;
			},
			/**
			 * Sends a chat message to the server.
			 */
			sendMessage() {
				if (this.messageContent && this.stompClient) {
					var chatMessage = {
						sender: this.username,
						content: this.messageContent,
						type: "CHAT"
					};
					this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
					this.messageContent = "";
				}
			},
			/**
			 * Handles incoming messages from the WebSocket.
			 */
			onMessageReceived(payload) {
				var message = JSON.parse(payload.body);

				// If it's a JOIN or LEAVE event, update the online users list
				if (message.type === "JOIN" || message.type === "LEAVE") {
					try {
						// Parse the content string (e.g., "[user1, user2]") into an array
						var content = message.content.replace("[", "").replace("]", "");
						if (content.trim() === "") {
							this.onlineUsers = [];
						} else {
							this.onlineUsers = content.split(",").map((u) => u.trim());
						}
					} catch (e) {
						console.error("Error parsing user list", e);
					}
				}

				// Add the message to the chat history
				this.messages.push(message);

				// Auto-scroll to the bottom of the chat area
				this.$nextTick(() => {
					var container = this.$refs.messageArea;
					if (container) container.scrollTop = container.scrollHeight;
				});
			}
		}
	};
</script>

<style scoped>
	.chat-container {
		max-width: 900px;
		margin: 20px auto;
	}
	.message-area {
		height: 400px;
		overflow-y: auto;
		border: 1px solid #eee;
		padding: 10px;
		background: #f9f9f9;
	}
	.event-message {
		color: #777;
		text-align: center;
		font-style: italic;
		margin: 5px 0;
	}
	.chat-message {
		margin-bottom: 10px;
	}
	.chat-message strong {
		color: #007bff;
	}
</style>
