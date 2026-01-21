package com.chatbot;

import java.util.*;
import java.util.regex.*;

public class ChatbotEngine {
    private Map<Pattern, String[]> responses;
    private Random random;
    private Map<String, Object> context;
    
    public ChatbotEngine() {
        this.responses = new HashMap<>();
        this.random = new Random();
        this.context = new HashMap<>();
        initializeResponses();
    }
    
    private void initializeResponses() {
        // Greetings
        addResponse("(?i).*(hello|hi|hey|greetings|good morning|good afternoon).*", 
            "Hello! 👋 How can I help you today?",
            "Hi there! 😊 What can I do for you?",
            "Hey! Nice to chat with you!",
            "Greetings! I'm here to assist you.");
        
        // How are you
        addResponse("(?i).*how are you.*",
            "I'm doing great, thanks for asking! 😊 How about you?",
            "I'm functioning perfectly! How are you doing today?",
            "Excellent! Ready to help you with anything you need!");
        
        // Name related
        addResponse("(?i).*what.*your name.*",
            "I'm ChatBot AI, your friendly virtual assistant! 🤖",
            "You can call me ChatBot! I'm here to help you.",
            "My name is ChatBot AI. Nice to meet you!");
        
        addResponse("(?i).*my name is ([A-Za-z]+).*",
            "Nice to meet you, {1}! 😊",
            "Hello {1}! Great to know your name!",
            "Pleased to meet you, {1}!");
        
        // Help
        addResponse("(?i).*(help|what can you do|capabilities).*",
            "I can help you with: ℹ️<br>• Answering questions<br>• Having conversations<br>• Telling jokes<br>• Basic math calculations<br>• Weather info<br>• Time & date<br>Just ask me anything!",
            "I'm here to assist! I can chat, answer questions, solve math problems, tell jokes, and more. What would you like to do?");
        
        // Jokes
        addResponse("(?i).*(joke|funny|laugh|humor).*",
            "Why don't scientists trust atoms? Because they make up everything! 😄",
            "What do you call a bear with no teeth? A gummy bear! 🐻",
            "Why did the scarecrow win an award? He was outstanding in his field! 🌾",
            "What do you call a fake noodle? An impasta! 🍝",
            "Why don't eggs tell jokes? They'd crack each other up! 🥚");
        
        // Weather
        addResponse("(?i).*(weather|temperature|forecast).*",
            "I don't have real-time weather data, but you can check <a href='https://weather.com' target='_blank'>weather.com</a> for accurate forecasts! ☀️",
            "For current weather information, I recommend checking your local weather service or weather apps! 🌤️");
        
        // Time and Date
        addResponse("(?i).*(what time|current time|time is it).*",
            "Current time: " + new Date().toString() + " ⏰",
            "The time right now is: " + new Date().toString());
        
        addResponse("(?i).*(what.*date|today.*date).*",
            "Today's date is: " + new Date().toString().substring(0, 10) + " 📅");
        
        // Thanks
        addResponse("(?i).*(thank|thanks|appreciate).*",
            "You're welcome! 😊",
            "Happy to help! 🎉",
            "Anytime! Feel free to ask more questions.",
            "My pleasure! Let me know if you need anything else.");
        
        // Goodbye
        addResponse("(?i).*(bye|goodbye|see you|exit|quit).*",
            "Goodbye! Have a great day! 👋",
            "See you later! Take care! 😊",
            "Bye! Come back anytime!",
            "Farewell! It was nice chatting with you!");
        
        // Age
        addResponse("(?i).*how old are you.*",
            "I'm timeless! Age is just a number for AI. 🤖",
            "I was created recently, so I'm pretty new to this world!");
        
        // Creator
        addResponse("(?i).*(who created|who made|developer).*",
            "I was created by a talented developer using Java and JSP! 👨‍💻",
            "A skilled programmer built me using Java technologies!");
        
        // Love/Like
        addResponse("(?i).*(i love|i like) (.+)",
            "That's awesome! {2} sounds great! 😊",
            "I'm glad you enjoy {2}! Tell me more about it.");
        
        // Capabilities
        addResponse("(?i).*(are you (smart|intelligent|ai)).*",
            "I'm an AI chatbot powered by pattern matching and machine learning concepts! 🧠",
            "Yes, I'm an artificial intelligence designed to have conversations and help you!");
        
        // Hobbies
        addResponse("(?i).*(hobby|hobbies|free time).*",
            "I enjoy chatting with people like you! It's what I do best. 💬",
            "My hobby is learning from conversations and helping people!");
    }
    
    private void addResponse(String pattern, String... possibleResponses) {
        responses.put(Pattern.compile(pattern), possibleResponses);
    }
    
    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "I didn't catch that. Could you say something? 🤔";
        }
        
        input = input.trim();
        
        // Store user name in context
        Pattern namePattern = Pattern.compile("(?i).*my name is ([A-Za-z]+).*");
        Matcher nameMatcher = namePattern.matcher(input);
        if (nameMatcher.matches()) {
            context.put("userName", nameMatcher.group(1));
        }
        
        // Check for math expression
        if (input.matches(".*\\d+\\s*[+\\-*/]\\s*\\d+.*")) {
            String mathResult = evaluateSimpleMath(input);
            if (mathResult != null) {
                return mathResult;
            }
        }
        
        // Find matching pattern
        for (Map.Entry<Pattern, String[]> entry : responses.entrySet()) {
            Matcher matcher = entry.getKey().matcher(input);
            if (matcher.matches()) {
                String[] possibleResponses = entry.getValue();
                String response = possibleResponses[random.nextInt(possibleResponses.length)];
                
                // Replace placeholders with matched groups
                if (matcher.groupCount() > 0) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        if (matcher.group(i) != null) {
                            response = response.replace("{" + i + "}", matcher.group(i));
                        }
                    }
                }
                return response;
            }
        }
        
        // Default responses for unmatched input
        String[] defaultResponses = {
            "That's interesting! Tell me more. 🤔",
            "I see. What else would you like to talk about?",
            "Hmm, I'm not sure about that. Can you ask me something else?",
            "I don't have a specific answer for that, but I'm here to chat! 💬",
            "That's a great point! What else is on your mind?",
            "Interesting! Could you rephrase that or ask something else?"
        };
        return defaultResponses[random.nextInt(defaultResponses.length)];
    }
    
    private String evaluateSimpleMath(String input) {
        try {
            Pattern mathPattern = Pattern.compile("(\\d+)\\s*([+\\-*/])\\s*(\\d+)");
            Matcher matcher = mathPattern.matcher(input);
            
            if (matcher.find()) {
                double num1 = Double.parseDouble(matcher.group(1));
                String operator = matcher.group(2);
                double num2 = Double.parseDouble(matcher.group(3));
                
                double result = 0;
                switch (operator) {
                    case "+": result = num1 + num2; break;
                    case "-": result = num1 - num2; break;
                    case "*": result = num1 * num2; break;
                    case "/": 
                        if (num2 == 0) return "I can't divide by zero! ❌";
                        result = num1 / num2; 
                        break;
                }
                return String.format("The answer is: %.2f 🔢", result);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    public void clearContext() {
        context.clear();
    }
}
