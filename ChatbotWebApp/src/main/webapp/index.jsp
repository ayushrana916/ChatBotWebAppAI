<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Chatbot - Java & JSP</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: black;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .chat-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            width: 100%;
            max-width: 800px;
            height: 600px;
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }
        
        .chat-header {
            background: grey;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 20px 20px 0 0;
        }
        
        .chat-header h1 {
            font-size: 24px;
            margin-bottom: 5px;
        }
        
        .chat-header p {
            font-size: 14px;
            opacity: 0.9;
        }
        
        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 20px;
            background: #f5f5f5;
        }
        
        .message {
            margin-bottom: 15px;
            display: flex;
            align-items: flex-start;
            animation: fadeIn 0.3s ease;
        }
        
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .message.user {
            justify-content: flex-end;
        }
        
        .message-content {
            max-width: 70%;
            padding: 12px 16px;
            border-radius: 18px;
            word-wrap: break-word;
        }
        
        .message.bot .message-content {
            background: white;
            color: #333;
            border: 1px solid #e0e0e0;
            margin-left: 10px;
        }
        
        .message.user .message-content {
            background: grey;
            color: white;
            margin-right: 10px;
        }
        
        .avatar {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            flex-shrink: 0;
        }
        
        .bot-avatar {
            background: green;
            color: white;
        }
        
        .user-avatar {
            background: #4CAF50;
            color: white;
        }
        
        .chat-input-container {
            padding: 20px;
            background: white;
            border-top: 1px solid #e0e0e0;
        }
        
        .chat-input-form {
            display: flex;
            gap: 10px;
        }
        
        .chat-input {
            flex: 1;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 25px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }
        
        .chat-input:focus {
            border-color: black;
        }
        
        .send-btn, .clear-btn {
            padding: 12px 24px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .send-btn {
            background: green;
            color: white;
        }
        
        .send-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .clear-btn {
            background: #f44336;
            color: white;
        }
        
        .clear-btn:hover {
            background: #d32f2f;
        }
        
        .timestamp {
            font-size: 11px;
            color: #999;
            margin-top: 4px;
        }
        
        .welcome-message {
            text-align: center;
            padding: 40px 20px;
            color: #666;
        }
        
        .welcome-message h2 {
            color: grey;
            margin-bottom: 10px;
        }
        
        .suggestions {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
        }
        
        .suggestion-chip {
            background: white;
            border: 1px solid #black;
            color: black;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 13px;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .suggestion-chip:hover {
            background: black;
            color: white;
        }
    </style>
</head>
<body>
    <div class="chat-container">
        <div class="chat-header">
            <h1>🤖 AI Chatbot</h1>
            <p>Powered by Java & JSP</p>
        </div>
        
        <div class="chat-messages" id="chatMessages">
            <%
                List<Map<String, String>> chatHistory = 
                    (List<Map<String, String>>) session.getAttribute("chatHistory");
                
                if (chatHistory == null) {
                    chatHistory = new ArrayList<>();
                    session.setAttribute("chatHistory", chatHistory);
                }
                
                String userMessage = (String) request.getAttribute("userMessage");
                String botResponse = (String) request.getAttribute("botResponse");
                
                if (userMessage != null && botResponse != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String timestamp = sdf.format(new Date());
                    
                    Map<String, String> userMsg = new HashMap<>();
                    userMsg.put("type", "user");
                    userMsg.put("message", userMessage);
                    userMsg.put("time", timestamp);
                    chatHistory.add(userMsg);
                    
                    Map<String, String> botMsg = new HashMap<>();
                    botMsg.put("type", "bot");
                    botMsg.put("message", botResponse);
                    botMsg.put("time", timestamp);
                    chatHistory.add(botMsg);
                }
                
                if (chatHistory.isEmpty()) {
            %>
                <div class="welcome-message">
                    <h2>Welcome to AI Chatbot!</h2>
                    <p>I'm here to help you. Try asking me something!</p>
                    <div class="suggestions">
                        <span class="suggestion-chip" onclick="sendSuggestion('Hello!')">👋 Say Hello</span>
                        <span class="suggestion-chip" onclick="sendSuggestion('Tell me a joke')">😄 Tell a Joke</span>
                        <span class="suggestion-chip" onclick="sendSuggestion('What can you do?')">❓ What can you do?</span>
                        <span class="suggestion-chip" onclick="sendSuggestion('Calculate 25 + 17')">🔢 Math Problem</span>
                    </div>
                </div>
            <%
                } else {
                    for (Map<String, String> msg : chatHistory) {
                        String type = msg.get("type");
                        String message = msg.get("message");
                        String time = msg.get("time");
            %>
                <div class="message <%= type %>">
                    <% if ("bot".equals(type)) { %>
                        <div class="avatar bot-avatar">🤖</div>
                    <% } %>
                    <div class="message-content">
                        <%= message %>
                        <div class="timestamp"><%= time %></div>
                    </div>
                    <% if ("user".equals(type)) { %>
                        <div class="avatar user-avatar">👤</div>
                    <% } %>
                </div>
            <%
                    }
                }
            %>
        </div>
        
        <div class="chat-input-container">
            <form class="chat-input-form" method="post" action="chat" id="chatForm">
                <input 
                    type="text" 
                    name="message" 
                    class="chat-input" 
                    placeholder="Type your message here..." 
                    autocomplete="off"
                    required
                    id="messageInput">
                <button type="submit" class="send-btn">Send 📤</button>
                <button type="button" class="clear-btn" onclick="clearChat()">Clear 🗑️</button>
            </form>
        </div>
    </div>
    
    <script>
        // Auto-scroll to bottom
        const chatMessages = document.getElementById('chatMessages');
        chatMessages.scrollTop = chatMessages.scrollHeight;
        
        // Focus input field
        document.getElementById('messageInput').focus();
        
        // Send suggestion
        function sendSuggestion(text) {
            document.getElementById('messageInput').value = text;
            document.getElementById('chatForm').submit();
        }
        
        // Clear chat
        function clearChat() {
            if (confirm('Are you sure you want to clear the chat history?')) {
                window.location.href = 'chat?action=clear';
            }
        }
    </script>
</body>
</html>