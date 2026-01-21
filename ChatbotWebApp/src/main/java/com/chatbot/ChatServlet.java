package com.chatbot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userMessage = request.getParameter("message");
        
        // Get or create chatbot engine in session
        HttpSession session = request.getSession();
        ChatbotEngine bot = (ChatbotEngine) session.getAttribute("chatbot");
        
        if (bot == null) {
            bot = new ChatbotEngine();
            session.setAttribute("chatbot", bot);
        }
        
        String botResponse = bot.getResponse(userMessage);
        
        // Set response attributes
        request.setAttribute("userMessage", userMessage);
        request.setAttribute("botResponse", botResponse);
        
        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("chatbot.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Clear chat action
        String action = request.getParameter("action");
        if ("clear".equals(action)) {
            HttpSession session = request.getSession();
            session.removeAttribute("chatbot");
            session.removeAttribute("chatHistory");
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("chatbot.jsp");
        dispatcher.forward(request, response);
    }
}

