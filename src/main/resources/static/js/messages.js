document.addEventListener("DOMContentLoaded", function () {
    const senderEmail = document.getElementById("senderEmail").value;
    let receiverEmail = document.getElementById("receiverEmail").value;

    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    // Connect to WebSocket
    stompClient.connect({}, function () {
        console.log("Connected to WebSocket");

        // Subscribe to receive messages (for both sender and receiver)
        stompClient.subscribe(`/topic/messages`, function (message) {
            const receivedMessage = JSON.parse(message.body);

            // Only display message if it's relevant to the current chat
            if (receivedMessage.senderEmail === receiverEmail || receivedMessage.receiverEmail === receiverEmail) {
                displayMessage(receivedMessage);
            }
        });
    });

    // Function to send a message
    function sendMessage() {
        const messageContent = document.getElementById("messageInput").value;

        if (messageContent.trim() !== "") {
            const message = {
                senderEmail: senderEmail,
                receiverEmail: receiverEmail,
                content: messageContent
            };

            // Send message via WebSocket
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));

            // Display message instantly for the sender (even before it reaches the receiver)
            displayMessage(message);

            // Clear message input field
            document.getElementById("messageInput").value = "";
        }
    }

    // Function to display a message in chat
    function displayMessage(message) {
        const chatBox = document.getElementById("chatBox");
        const messageElement = document.createElement("div");

        // Add class based on whether the message was sent or received
        messageElement.classList.add("message", message.senderEmail === senderEmail ? "sent" : "received");
        messageElement.innerHTML = `${message.content}`;
        chatBox.appendChild(messageElement);
        chatBox.scrollTop = chatBox.scrollHeight; // Scroll to bottom to show latest message
    }

    // Add event listener for the send button
    document.getElementById("sendMessageBtn").addEventListener("click", sendMessage);
});
