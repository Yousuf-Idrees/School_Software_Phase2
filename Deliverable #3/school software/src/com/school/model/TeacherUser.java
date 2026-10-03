package com.school.model;

// Concrete Colleague 2: Teacher
public class TeacherUser extends ChatUser {

    public TeacherUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    public void send(String message) {
        System.out.println("[" + name + " - Teacher] Sending: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public void receive(String message, String senderName) {
        System.out.println("[" + name + " - Teacher] Received from " + senderName + ": " + message);
    }
}
