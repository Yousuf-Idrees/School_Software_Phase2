package com.school.model;

// Concrete Colleague 1: Student
public class StudentUser extends ChatUser {

    public StudentUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    public void send(String message) {
        System.out.println("[" + name + " - Student] Sending: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public void receive(String message, String senderName) {
        System.out.println("[" + name + " - Student] Received from " + senderName + ": " + message);
    }
}
