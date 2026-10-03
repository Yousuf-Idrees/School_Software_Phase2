package com.school.model;

// Mediator Interface
public interface ChatMediator {
    void sendMessage(String message, ChatUser sender);
    void addUser(ChatUser user);
}
