package com.school.model;

// Abstract Colleague
public abstract class ChatUser {
    protected ChatMediator mediator;
    protected String name;

    public ChatUser(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() { 
        return name; 
    }

    public abstract void send(String message);
    public abstract void receive(String message, String senderName);
}
