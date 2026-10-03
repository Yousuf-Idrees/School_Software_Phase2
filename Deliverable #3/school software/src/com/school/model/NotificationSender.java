package com.school.model;

// Context
public class NotificationSender {
    private NotificationDeliveryStrategy strategy;

    public NotificationSender(NotificationDeliveryStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(NotificationDeliveryStrategy strategy) {
        this.strategy = strategy;
    }

    public void send(String recipient, String message) {
        strategy.sendNotification(recipient, message);
    }
}
