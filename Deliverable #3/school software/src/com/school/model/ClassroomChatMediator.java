package com.school.model;

import java.util.ArrayList;
import java.util.List;

// Concrete Mediator
public class ClassroomChatMediator implements ChatMediator {

    private List<ChatUser> users = new ArrayList<>();

    @Override
    public void addUser(ChatUser user) {
        users.add(user);
    }

    @Override
    public void sendMessage(String message, ChatUser sender) {
        for (ChatUser user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}
